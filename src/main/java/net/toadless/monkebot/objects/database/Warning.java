package net.toadless.monkebot.objects.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.pojos.Warnings;
import net.toadless.monkebot.util.DatabaseUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Warning
{
    private static final Map<Long, Integer> cachedWarns = new HashMap<>();
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

    public void add(int amount)
    {
        var warns = get();

        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var guilds = collection.find(new Document("guildId", guildId).append("userId", userId));

            Bson filter = and(eq("guildId", guildId), eq("userId", userId));

            if (guilds.first() == null) collection.insertOne(new Warnings(guildId, userId, warns + amount));
            else collection.updateOne(filter, new Document("$set", new Document("amount", warns + amount)));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }

        cachedWarns.put(DatabaseUtils.getWarnId(Objects.requireNonNull(Objects.requireNonNull(monke.getShardManager().getGuildById(guildId)).getMemberById(userId))), warns + amount);
    }

    public void remove(int amount)
    {
        var warns = get();

        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var guilds = collection.find(new Document("guildId", guildId).append("userId", userId));

            Bson filter = and(eq("guildId", guildId), eq("userId", userId));

            if (guilds.first() == null) collection.insertOne(new Warnings(guildId, userId, warns - amount));
            else collection.updateOne(filter, new Document("$set", new Document("amount", warns - amount)));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }

        cachedWarns.put(DatabaseUtils.getWarnId(Objects.requireNonNull(Objects.requireNonNull(monke.getShardManager().getGuildById(guildId)).getMemberById(userId))), warns - amount);
    }

    public int get()
    {
        var warnId = DatabaseUtils.getWarnId(Objects.requireNonNull(Objects.requireNonNull(monke.getShardManager().getGuildById(guildId)).getMemberById(userId)));

        if (isPresent(warnId)) return cachedWarns.get(warnId);

        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Warning.collection, Warnings.class);
            var warns = collection.find(new Document("guildId", guildId).append("userId", userId));

            if (warns.first() == null) { collection.insertOne(new Warnings(guildId, userId, 0)); return 0; }

            return Objects.requireNonNull(warns.first()).getAmount();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
            return 0;
        }
    }

    public boolean isPresent(long warnId)
    {
        return cachedWarns.containsKey(warnId);
    }
}