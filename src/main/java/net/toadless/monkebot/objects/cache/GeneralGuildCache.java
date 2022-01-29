package net.toadless.monkebot.objects.cache;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.pojos.ChannelBlacklists;
import net.toadless.monkebot.objects.pojos.WordBlacklists;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeneralGuildCache
{
    private static final Map<Long, GeneralGuildCache> GUILD_CACHES = new ConcurrentHashMap<>();

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
