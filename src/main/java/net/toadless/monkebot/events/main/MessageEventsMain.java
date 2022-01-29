package net.toadless.monkebot.events.main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.cache.CachedMessage;
import net.toadless.monkebot.objects.cache.MessageCache;
import net.toadless.monkebot.util.BlacklistUtils;

public class MessageEventsMain extends ListenerAdapter
{
    private final Monke monke;

    public MessageEventsMain(Monke monke)
    {
        this.monke = monke;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.isFromGuild() && !event.getAuthor().isBot() && !event.isWebhookMessage())
        {
            Guild guild = event.getGuild();

            if (BlacklistUtils.isChannelBlacklisted(event, monke))
            {
                return;
            }

            MessageCache.getCache(guild.getIdLong()).put(new CachedMessage(event.getMessage()));
        }

        monke.getCommandHandler().handleEvent(event);
    }
}