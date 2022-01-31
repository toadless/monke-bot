package net.toadless.monkebot.events.main;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.cache.CachedMessage;
import net.toadless.monkebot.objects.cache.MessageCache;
import net.toadless.monkebot.objects.database.Vote;
import net.toadless.monkebot.util.BlacklistUtils;
import net.toadless.monkebot.util.EmbedUtils;

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

        if (handleVote(event))
        {
            return;
        }

        monke.getCommandHandler().handleEvent(event);
    }

    private boolean handleVote(MessageReceivedEvent event)
    {
        if (!event.getChannelType().equals(ChannelType.PRIVATE) && !event.getAuthor().isBot())
        {
            return false;
        }
        if (event.getMessage().getReferencedMessage() == null)
        {
            return false;
        }

        String content = event.getMessage().getContentRaw();
        long messageId = event.getMessage().getReferencedMessage().getIdLong();
        boolean isRunning = Vote.isVoteRunning(messageId, monke);

        if (isRunning)
        {
            String[] opts = content.split(" ");

            if (opts.length < 1)
            {
                EmbedUtils.sendError(event.getChannel(), "You need to enter an option to vote for.");
                return true;
            }

            try
            {
                int maxOptions = Vote.getMaxVoteById(messageId, monke);
                int option;

                if (opts[0].startsWith("abstain"))
                {
                    option = -2;
                }
                else
                {
                    option = Integer.parseInt(opts[0]);
                    if (option < 0)
                    {
                        EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
                        return true;
                    }
                    if (option > maxOptions)
                    {
                        EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
                        return true;
                    }
                }

                if (Vote.castById(event.getAuthor().getIdLong(), messageId, option, monke))
                {
                    EmbedUtils.sendSuccess(event.getChannel(), "Vote cast!");
                    return true;
                }
            }
            catch (Exception exception)
            {
                EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
                return true;
            }
        }

        return true;
    }
}