package net.toadless.monkebot.objects.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.pojos.ReactionRoles;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class ReactionRole
{
    private static final String collection = "reactionroles";

    private final long messageId;
    private final long roleId;
    private final String emote;
    private final Monke monke;
    private final long guildId;

    public ReactionRole(long messageId, long roleId, long guildId, @NotNull String emote, @NotNull Monke monke)
    {
        this.messageId = messageId;
        this.roleId = roleId;
        this.guildId = guildId;
        this.emote = emote;
        this.monke = monke;
    }

    public static @NotNull List<ReactionRole> getByMessageId(long messageId, @NotNull Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(ReactionRole.collection, ReactionRoles.class);
            var filter = and(eq("messageId", messageId));
            var result = collection.find(filter);

            if (result.first() == null)
            {
                return Collections.emptyList();
            }
            else
            {
                List<ReactionRole> reactionRoles = new ArrayList<>();
                for (var rr : result)
                {
                    reactionRoles.add(new ReactionRole(rr.getMessageId(), rr.getRoleId(), rr.getGuildId(), rr.getEmoteId(), monke));
                }
                return reactionRoles;
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
            return Collections.emptyList();
        }
    }

    public void add()
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(ReactionRole.collection, ReactionRoles.class);
            var document = new Document("messageId", messageId).append("roleId", roleId).append("guildId", guildId).append("emoteId", emote);
            var guilds = collection.find(document);

            if (guilds.first() == null) collection.insertOne(new ReactionRoles(messageId, guildId, emote, roleId));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }
    }

    public long getMessageId()
    {
        return messageId;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public @NotNull String getEmote()
    {
        return emote;
    }

    public long getGuildId()
    {
        return guildId;
    }

    public void addRole(@NotNull Member member)
    {
        Guild guild = monke.getShardManager().getGuildById(guildId);
        if (guild != null)
        {
            Role role = guild.getRoleById(roleId);
            if (role != null)
            {
                guild.addRoleToMember(member, role).queue();
            }
        }
    }

    public void removeRole(@NotNull Member member)
    {
        Guild guild = monke.getShardManager().getGuildById(guildId);
        if (guild != null)
        {
            Role role = guild.getRoleById(roleId);
            if (role != null)
            {
                guild.removeRoleFromMember(member, role).queue();
            }
        }
    }

    public void remove()
    {
        try
        {
            try
            {
                var connection = monke.getDatabaseHandler().getConnection();
                var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
                var collection = database.getCollection(ReactionRole.collection, ReactionRoles.class);
                var document = new Document("messageId", messageId).append("roleId", roleId).append("guildId", guildId).append("emoteId", emote);

                collection.findOneAndDelete(document);
            }
            catch (Exception exception)
            {
                monke.getLogger().error("A database error occurred", exception);
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }
    }

    public boolean isPresent()
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(ReactionRole.collection, ReactionRoles.class);
            var document = new Document("messageId", messageId).append("roleId", roleId).append("guildId", guildId).append("emoteId", emote);
            var guilds = collection.find(document);
            return !(guilds.first() == null);
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
            return false;
        }
    }
}