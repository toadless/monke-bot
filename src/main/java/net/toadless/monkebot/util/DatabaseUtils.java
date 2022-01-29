package net.toadless.monkebot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.cache.GeneralGuildCache;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.database.Tempban;
import net.toadless.monkebot.objects.pojos.ChannelBlacklists;
import net.toadless.monkebot.objects.pojos.Tempbans;
import net.toadless.monkebot.objects.pojos.WordBlacklists;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

    private DatabaseUtils()
    {
        //Overrides the default, public, constructor
    }

    public static void registerGuild(Guild guild, Monke monke)
    {
        LOGGER.debug("Registered guild " + guild.getId());
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(GuildSettingsCache.collection);
            var guilds = collection.find(new Document("_id", guild.getIdLong()));

            if (guilds.first() == null) collection.insertOne(new Document("_id", guild.getIdLong()));

            syncBlacklistedChannels(guild, monke);
            syncBlacklistedPhrases(guild, monke);
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }
    }

    public static List<Tempbans> getExpiredTempbans(Monke monke)
    {
        List<Tempbans> result = new ArrayList<>();
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Tempban.collection, Tempbans.class);
            var guilds = collection.find();

            for (var value : guilds)
            {
                if (value.getMutedUntil().isBefore(LocalDateTime.now()))
                {
                    result.add(new Tempbans(value.getUserId(), value.getGuildId(), value.getMutedUntil()));
                }
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
        return result;
    }

    public static void removeGuild(Guild guild, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(GuildSettingsCache.collection);
            collection.findOneAndDelete(eq("_id", guild.getIdLong()));

            GuildSettingsCache.removeCache(guild.getIdLong());
            GeneralGuildCache.removeCache(guild.getIdLong());
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }
    }


    private static void syncBlacklistedPhrases(Guild guild, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.wordCollection, WordBlacklists.class);
            var document = new Document("guildId", guild.getIdLong());
            var query = collection.find(document);

            GeneralGuildCache generalGuildCache = GeneralGuildCache.getCache(guild.getIdLong(), monke);

            for (var row : query)
            {
                generalGuildCache.addBlacklistedPhrase(row.getPhrase());
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }

    private static void syncBlacklistedChannels(Guild guild, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.channelCollection, ChannelBlacklists.class);
            var document = new Document("guildId", guild.getIdLong());
            var query = collection.find(document);

            GeneralGuildCache generalGuildCache = GeneralGuildCache.getCache(guild.getIdLong(), monke);

            for (var row : query)
            {
                generalGuildCache.addBlacklistedChannel(monke.getShardManager().getGuildById(guild.getIdLong()).getTextChannelById(row.getChannelId()));
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }
}