package net.toadless.monkebot.objects.database;

import java.time.LocalDateTime;

import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.pojos.Tempbans;
import org.bson.Document;

public class Tempban
{
    public static final String collection = "tempbans";

    private final Monke monke;
    private final Long userId;
    private final Long guildId;

    public Tempban(Monke monke, Long userId, Long guildId)
    {
        this.monke = monke;
        this.userId = userId;
        this.guildId = guildId;
    }

    public boolean remove()
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Tempban.collection, Tempbans.class);
            var document = new Document("userId", userId).append("guildId", guildId);
            var guilds = collection.find(document);

            if (guilds.first() == null) return false;
            collection.findOneAndDelete(document);

            var guild = monke.getShardManager().getGuildById(guildId);
            if (guild == null) return false;

            var role = guild.getRoleById(GuildSettingsCache.getCache(guildId, monke).getTempBanRole());
            if (role == null) return false;

            guild.retrieveMemberById(userId).queue(member -> guild.removeRoleFromMember(member, role).queue());
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
            return false;
        }

        return true;
    }

    public boolean add(LocalDateTime mutedUntil)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Tempban.collection, Tempbans.class);
            var document = new Document("userId", userId).append("guildId", guildId).append("mutedUntil", mutedUntil);
            var guilds = collection.find(document);

            if (guilds.first() != null) return false;
            collection.insertOne(new Tempbans(userId, guildId, mutedUntil));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
            return false;
        }
        return true;
    }
}