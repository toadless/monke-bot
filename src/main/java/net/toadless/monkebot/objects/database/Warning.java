package net.toadless.monkebot.objects.database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.pojos.Warnings;
import org.bson.Document;

public class Warning
{
    private static final String collection = "warns";

    private final long userId;
    private final long guildId;
    private final Monke monke;

    public Warning(Guild guild, User user, Monke monke)
    {
        this.guildId = guild.getIdLong();
        this.userId = user.getIdLong();
        this.monke = monke;
    }

    public void add(String reason)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var document = new Document("guildId", guildId).append("userId", userId);
            var result = collection.countDocuments(document);

            collection.insertOne(new Warnings(
                    result+1,
                    guildId,
                    userId,
                    LocalDateTime.now(),
                    reason
            ));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }


    public void remove(long key)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var document = new Document("guildId", guildId).append("userId", userId).append("_id", key);

            collection.findOneAndDelete(document);
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }

    public List<Warnings> get()
    {
        List<Warnings> result = new ArrayList<>();
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var document = new Document("guildId", guildId).append("userId", userId);
            var query = collection.find(document);

            for (var value : query)
            {
                result.add(new Warnings(value.getId(), value.getUserId(), value.getGuildId(), value.getTimestamp(), value.getWarnText()));
            }

        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }

        return result;
    }

    public Warnings getByWarnId(long warnId)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var document = new Document("guildId", guildId).append("userId", userId).append("_id", warnId);
            var result = collection.find(document).first();

            if (result != null)
            {
                var warn = result;
                return new Warnings(warn.getId(), warn.getUserId(), warn.getGuildId(), warn.getTimestamp(), warn.getWarnText());
            }
            else
            {
                return null;
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
            return null;
        }
    }

    public boolean isPresent(long warnId)
    {
        return getByWarnId(warnId) != null;
    }
}