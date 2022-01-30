package net.toadless.monkebot.objects.cache;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.util.DatabaseUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GeneralGuildCache
{
    private static final Map<Long, GeneralGuildCache> GUILD_CACHES = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .expiration(30, TimeUnit.MINUTES)
            .build();

    private final List<String> blacklistedPhrases;
    private final List<MessageChannel> blacklistedChannels;

    private final Monke monke;
    private final Long guildId;

    public GeneralGuildCache(Long guildId, Monke monke)
    {
        this.monke = monke;
        this.guildId = guildId;

        this.blacklistedPhrases = new ArrayList<>();
        this.blacklistedChannels = new ArrayList<>();
    }

    public static @NotNull GeneralGuildCache getCache(long  guildId, Monke monke)
    {
        GeneralGuildCache cache = GUILD_CACHES.get(guildId);
        if (GUILD_CACHES.get(guildId) == null)
        {
            cache = new GeneralGuildCache(guildId, monke);
            GUILD_CACHES.put(guildId, cache);

            DatabaseUtils.syncBlacklistedChannels(guildId, monke);
            DatabaseUtils.syncBlacklistedPhrases(guildId, monke);
        }
        return cache;
    }

    public static void removeCache(long guildId)
    {
        GUILD_CACHES.remove(guildId);
    }

    public List<MessageChannel> getBlacklistedChannels()
    {
        return blacklistedChannels;
    }

    public List<String> getBlacklistedPhrases()
    {
        return blacklistedPhrases;
    }

    public void addBlacklistedChannel(MessageChannel blacklistedChannel)
    {
        this.blacklistedChannels.add(blacklistedChannel);
    }

    public void addBlacklistedPhrase(String blacklistedPhrase)
    {
        this.blacklistedPhrases.add(blacklistedPhrase);
    }

    public void removeBlacklistedChannel(MessageChannel blacklistedChannel)
    {
        this.blacklistedChannels.remove(blacklistedChannel);
    }

    public void removeBlacklistedPhrase(String blacklistedPhrase)
    {
        this.blacklistedPhrases.remove(blacklistedPhrase);
    }
}