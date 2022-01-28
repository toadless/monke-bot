package net.toadless.monkebot.objects.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.bot.ConfigOption;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import static com.mongodb.client.model.Filters.eq;

public class GuildSettingsCache implements ICache<String, CachedGuildSetting>
{
    private static final Map<Long, GuildSettingsCache> GUILD_CACHES = new ConcurrentHashMap<>();
    public static final String collection = "guilds";

    private final Map<String, CachedGuildSetting> cachedValues;

    private final Monke monke;
    private final Long guildId;

    public GuildSettingsCache(Long guildId, Monke monke)
    {
        this.monke = monke;
        this.guildId = guildId;
        this.cachedValues = ExpiringMap.builder()
                .maxSize(50)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .expiration(30, TimeUnit.MINUTES)
                .build();
    }

    public static @NotNull GuildSettingsCache getCache(long  guildId, Monke monke)
    {
        GuildSettingsCache cache = GUILD_CACHES.get(guildId);
        if (GUILD_CACHES.get(guildId) == null)
        {
            cache = new GuildSettingsCache(guildId, monke);
            GUILD_CACHES.put(guildId, cache);
        }
        return cache;
    }

    public static void removeCache(long guildId)
    {
        GUILD_CACHES.remove(guildId);
    }

    @Override
    public void put(CachedGuildSetting value)
    {
        cachedValues.put(value.getKey(), value);
    }

    @Override
    public void put(Collection<CachedGuildSetting> values)
    {
        values.forEach(this::put);
    }

    @Override
    public @NotNull CachedGuildSetting get(String key)
    {
        return cachedValues.get(key);
    }

    @Override
    public void update(CachedGuildSetting oldValue, CachedGuildSetting newValue)
    {
        cachedValues.put(oldValue.getKey(), newValue);
    }

    @Override
    public void update(String oldValue, CachedGuildSetting newValue)
    {
        cachedValues.put(oldValue, newValue);
    }

    @Override
    public boolean isCached(String key)
    {
        return cachedValues.containsKey(key);
    }

    @Override
    public void remove(String key)
    {
        cachedValues.remove(key);
    }

    @Override
    public void remove(CachedGuildSetting key)
    {
        remove(key.getKey());
    }

    @Override
    public void remove(Collection<CachedGuildSetting> values)
    {
        values.forEach(this::remove);
    }

    public long getLogChannel()
    {
        return cacheGetLong("logchannel", -1L);
    }

    public void setLogChannel(long newChannel)
    {
        cachePut("logchannel", newChannel);
    }

    public long getTempBanRole()
    {
        return cacheGetLong("tempbanrole", -1L);
    }

    public void setTempBanRole(long newRole)
    {
        cachePut("tempbanrole", newRole);
    }

    public @NotNull String getPrefix()
    {
        return cacheGetString("prefix", monke.getConfiguration().getString(ConfigOption.PREFIX));
    }

    public void setPrefix(@NotNull String newPrefix)
    {
        cachePut("prefix", newPrefix);
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(String field, T value)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(GuildSettingsCache.collection);
            var guilds = collection.find(new Document("_id", guildId));

            if (guilds.first() == null) { collection.insertOne(new Document("_id", guildId).append(field, value)); return value; }
            else if (guilds.first().get(field) == null) { setField(field, value); return value; }

            return (T) guilds.first().get(field);
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
            return null;
        }
    }

    private <T> long cacheGetLong(String label, T value)
    {
        if (cachedValues.get(label) == null)
        {
            cachedValues.put(label, new CachedGuildSetting(label, String.valueOf(getField(label, value))));
        }
        try
        {
            return Long.parseLong(cachedValues.get(label).getValue());
        }
        catch (Exception exception)
        {
            return -1;
        }
    }

    private <T> @NotNull String cacheGetString(String label, T value)
    {
        if (cachedValues.get(label) == null)
        {
            cachedValues.put(label, new CachedGuildSetting(label, String.valueOf(getField(label, value))));
        }
        return cachedValues.get(label).getValue();
    }

    private <T> void cachePut(String label, T newValue)
    {
        update(label, new CachedGuildSetting(label, String.valueOf(newValue)));
        setField(label, newValue);
    }


    private <T> void setField(String field, T value)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(GuildSettingsCache.collection);
            var guilds = collection.find(new Document("_id", guildId));

            if (guilds.first() == null) collection.insertOne(new Document("_id", guildId).append(field, value));
            else collection.updateOne(eq("_id", guildId), new Document("$set", new Document(field, String.valueOf(value))));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A database error occurred", exception);
        }
    }
}