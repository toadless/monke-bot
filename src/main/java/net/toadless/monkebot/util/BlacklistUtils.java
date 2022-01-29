package net.toadless.monkebot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.pojos.ChannelBlacklists;
import net.toadless.monkebot.objects.pojos.WordBlacklists;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BlacklistUtils
{
    public static final String channelCollection = "channelblacklists";
    public static final String wordCollection = "wordblacklists";

    public static final List<String> LINKS = List.copyOf(List.of("youtube.com", "twitch.tv", "youtu.be", "https://", "http://", "www."));
    public static final List<String> DISCORD = List.copyOf(List.of("discord.gg/"));

    private BlacklistUtils()
    {
        //Overrides the default, public, constructor
    }

    public static boolean isBlacklistedPhrase(MessageReceivedEvent event, Monke monke)
    {
        if (!event.isFromGuild())
        {
            return false;
        }

        Guild guild = event.getGuild();
        String content = event.getMessage().getContentRaw().toLowerCase();
        Member member = event.getMember();

        if (member == null)
        {
            return false;
        }

        return getBlacklistedPhrases(guild, monke).stream().anyMatch(phrase -> content.contains(phrase.toLowerCase()));
    }

    public static boolean isChannelBlacklisted(MessageReceivedEvent event, Monke monke)
    {
        if (!event.isFromGuild())
        {
            return false;
        }

        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.channelCollection, ChannelBlacklists.class);
            var document = new Document("guildId", event.getGuild().getIdLong()).append("channelId", event.getChannel().getIdLong());
            var result = collection.find(document);

            return result.first() != null;
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
            return false;
        }
    }

    public static List<String> getBlacklistedPhrases(Guild guild, Monke monke)
    {
        List<String> result = new ArrayList<>();
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.wordCollection, WordBlacklists.class);
            var document = new Document("guildId", guild.getIdLong());
            var query = collection.find(document);

            for (var row : query)
            {
                result.add(row.getPhrase());
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
        return result;
    }

    public static void addPhrase(Guild guild, String phrase, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.wordCollection, WordBlacklists.class);

            collection.insertOne(new WordBlacklists(
                    guild.getIdLong(),
                    phrase
            ));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }

    public static boolean addChannel(MessageChannel channel, Guild guild, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.channelCollection, ChannelBlacklists.class);
            var document = new Document("guildId", guild.getIdLong()).append("channelId", channel.getIdLong());
            var exists = collection.find(document);

            if (exists.first() != null)
            {
                return false;
            }

            collection.insertOne(new ChannelBlacklists(
                    guild.getIdLong(),
                    channel.getIdLong()
            ));
            return true;
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
            return false;
        }
    }

    public static List<ChannelBlacklists> getBlacklistedChannels(Guild guild, Monke monke)
    {
        List<ChannelBlacklists> result = new ArrayList<>();
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.channelCollection, ChannelBlacklists.class);
            var document = new Document("guildId", guild.getIdLong());
            var query = collection.find(document);

            for (var row : query)
            {
                result.add(new ChannelBlacklists(row.getGuildId(), row.getChannelId()));
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
        return result;
    }

    public static boolean removeChannel(MessageChannel channel, Guild guild, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.channelCollection, ChannelBlacklists.class);
            var document = new Document("guildId", guild.getIdLong()).append("channelId", channel.getIdLong());
            var delete = collection.deleteOne(document);

            return delete.wasAcknowledged();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
            return false;
        }
    }


    public static boolean removePhrase(Guild guild, String phrase, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(BlacklistUtils.wordCollection, WordBlacklists.class);
            var document = new Document("guildId", guild.getIdLong()).append("phrase", phrase);
            var delete = collection.deleteOne(document);

            return delete.wasAcknowledged();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
            return false;
        }
    }
}