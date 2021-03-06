package net.toadless.monkebot.commands.maincommands.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.objects.Emoji;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.cache.MessageCache;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.database.Report;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandHierarchyException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.objects.exception.CommandSyntaxException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import net.toadless.monkebot.util.StringUtils;
import net.toadless.monkebot.util.UserUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public class ReportCommand extends Command
{
    public ReportCommand()
    {
        super("Report", "Reports a member for a reason.", "[user] [reason]");
        addAliases("report");
        addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();
        Guild guild = event.getGuild();
        MessageChannel reportChannel = guild.getTextChannelById(GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke()).getReportChannel());

        if (CommandChecks.channelConfigured(reportChannel, "Report channel", failure)) return;
        if (CommandChecks.canSee(reportChannel, event.getSelfMember(), "Report channel", failure)) return;

        new Parser(args.get(0), event).parseAsUser(user ->
        {
            args.remove(0);
            String reason = StringUtils.markdownSanitize(String.join(" ", args));

            if (reason.isBlank())
            {
                failure.accept(new CommandSyntaxException(event));
                return;
            }

            String messageLink = StringUtils.getMessageLink(event.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong());

            if (user.isBot())
            {
                failure.accept(new CommandInputException("You may not report bots."));
                return;
            }

            UserUtils.getMemberFromUser(user, guild).queue(
                    member ->
                    {
                        if (member.isOwner())
                        {
                            failure.accept(new CommandHierarchyException(this));
                            return;
                        }

                        reportChannel.sendMessage(new EmbedBuilder()
                                .setTitle("New report by: " + author.getAsTag())
                                .addField("Reporting user:", user.getAsMention(), false)
                                .addField("Description:", reason, false)
                                .addField("Channel:", StringUtils.getChannelAsMention(channel.getId()), false)
                                .addField("Message Link:", "[Jump to message](" + messageLink + ")", false)
                                .setColor(member.getColor())
                                .setFooter("This report is unhandled and can only be dealt by members higher than " + member.getRoles().get(0).getName())
                                .build()).queue
                                (
                                        message ->
                                        {
                                            author.openPrivateChannel()
                                                    .flatMap(privateChannel ->
                                                            privateChannel.sendMessage(generateDM(member, user, reason).build()))
                                                    .queue(null, error ->
                                                    {
                                                    });

                                            Report.add(message.getIdLong(), event.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong(), user.getIdLong(), author.getIdLong(), reason, event.getMonke());
                                            message.addReaction(Emoji.THUMB_UP.getUnicode()).queue();
                                        }
                                );
                    }
            );
        });
    }

    private EmbedBuilder generateDM(Member author, User reported, String reason)
    {
        return new EmbedBuilder()
                .setTitle("Your report in " + author.getGuild().getName())
                .addField("Reporting User", reported.getAsMention(), true)
                .addField("Reason", reason, true)
                .setColor(Constants.EMBED_COLOUR)
                .setTimestamp(Instant.now());
    }
}
