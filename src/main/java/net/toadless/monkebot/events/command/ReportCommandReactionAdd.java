package net.toadless.monkebot.events.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.Emoji;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.database.Report;
import net.toadless.monkebot.util.EmbedUtils;
import net.toadless.monkebot.util.StringUtils;

import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class ReportCommandReactionAdd extends ListenerAdapter
{
    private final Monke monke;

    public ReportCommandReactionAdd(Monke monke)
    {
        this.monke = monke;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        if (!event.getReactionEmote().isEmoji() || !event.isFromGuild())
        {
            return;
        }

        if (!event.getReactionEmote().getEmoji().equalsIgnoreCase(Emoji.THUMB_UP.getUnicode()))
        {
            return;
        }

        Guild guild = event.getGuild();

        MessageChannel reportChannel = guild.getTextChannelById(GuildSettingsCache.getCache(guild.getIdLong(), monke).getReportChannel());

        if (reportChannel == null)
        {
            return;
        }

        List<RestAction<?>> actions = new ArrayList<>();

        actions.add(event.retrieveMessage());
        actions.add(event.retrieveUser());
        actions.add(event.retrieveMember());

        RestAction.allOf(actions).queue(
                results ->
                {
                    Message message = (Message) results.get(0);
                    User user = (User) results.get(1);
                    Member member = (Member) results.get(2);

                    if (user.equals(monke.getJDA().getSelfUser()))
                    {
                        return;
                    }
                    if (message.getEmbeds().isEmpty())
                    {
                        return;
                    }

                    Report report = Report.getById(message.getIdLong(), monke);

                    if (report != null)
                    {
                        Member reportedMember = guild.getMemberById(report.getReportedUserId());
                        if (reportedMember == null)
                        {
                            EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0))
                                    .setColor(Color.GREEN)
                                    .setFooter("This report was dealt with by " + user.getAsTag() + " the reported member left."));

                            Report.remove(report.getMessageId(), monke);
                            return;
                        }

                        if (!member.canInteract(reportedMember))
                        {
                            message.removeReaction(Emoji.THUMB_UP.getUnicode(), user).queue();
                            return;
                        }

                        EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0))
                                .setColor(Color.GREEN)
                                .setFooter("This report was dealt with by " + user.getAsTag()));

                        Report.remove(report.getMessageId(), monke);
                        message.clearReactions().queue();

                        User reportee = monke.getShardManager().getUserById(report.getReportingUserId());

                        if (reportee != null)
                        {
                            reportee.openPrivateChannel().flatMap(
                                    privateChannel ->

                                            privateChannel.sendMessage(new EmbedBuilder()
                                                    .setTitle("Your report in " + guild.getName())
                                                    .addField("Reporting User", StringUtils.getUserAsMention(report.getReportedUserId()), true)
                                                    .addField("Reason", report.getReason(), true)
                                                    .addField("Dealt with by", user.getAsMention(), true)
                                                    .setColor(Constants.EMBED_COLOUR)
                                                    .setTimestamp(Instant.now()).build())
                            ).queue(null, error ->
                            {
                            });
                        }
                    }
                }, error ->
                {
                });
    }
}
