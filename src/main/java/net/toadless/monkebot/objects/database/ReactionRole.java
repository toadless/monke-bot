package net.toadless.monkebot.objects.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.toadless.monkebot.Monke;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import static org.jooq.generated.tables.ReactionRoles.REACTION_ROLES;

public class ReactionRole
{
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
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            var query = context
                    .selectFrom(REACTION_ROLES)
                    .where(REACTION_ROLES.MESSAGE_ID.eq(messageId));

            var result = query.fetch();
            query.close();

            if (result.isEmpty())
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
            monke.getLogger().error("An SQL error occurred", exception);
            return Collections.emptyList();
        }
    }

    public void add()
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            var query = context
                    .insertInto(REACTION_ROLES)
                    .columns(REACTION_ROLES.GUILD_ID, REACTION_ROLES.MESSAGE_ID, REACTION_ROLES.ROLE_ID, REACTION_ROLES.EMOTE_ID)
                    .values(guildId, messageId, roleId, emote);
            query.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
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
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            var query = context
                    .deleteFrom(REACTION_ROLES)
                    .where(REACTION_ROLES.MESSAGE_ID.eq(messageId).and(REACTION_ROLES.ROLE_ID.eq(roleId)).and(REACTION_ROLES.EMOTE_ID.eq(emote)));
            query.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public boolean isPresent()
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            var query = context
                    .selectFrom(REACTION_ROLES)
                    .where(REACTION_ROLES.MESSAGE_ID.eq(messageId).and(REACTION_ROLES.EMOTE_ID.eq(emote)).and(REACTION_ROLES.ROLE_ID.eq(roleId)));

            var result = query.fetch();
            query.close();
            return !result.isEmpty();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
            return false;
        }
    }
}