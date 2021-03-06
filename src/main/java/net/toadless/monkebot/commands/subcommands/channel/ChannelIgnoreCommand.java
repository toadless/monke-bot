package net.toadless.monkebot.commands.subcommands.channel;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.util.BlacklistUtils;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class ChannelIgnoreCommand extends Command
{
    public ChannelIgnoreCommand(Command parent)
    {
        super(parent, "ignore", "Controls ignored channels.", "[channel][true / false]");
        addMemberPermissions(Permission.MANAGE_SERVER);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

        new Parser(args.get(0), event).parseAsTextChannel(
                channel ->
                {
                    if (channel.equals(event.getTextChannel()))
                    {
                        failure.accept(new CommandInputException("You cannot ignore me in the channel where you issued the command."));
                        return;
                    }

                    Optional<Boolean> bool = new Parser(args.get(1), event).parseAsBoolean();

                    if (bool.isPresent())
                    {
                        if (bool.get())
                        {
                            if (BlacklistUtils.addChannel(channel, event.getGuild(), event.getMonke()))
                            {
                                event.replySuccess("Blacklisted channel " + channel.getAsMention());
                            }
                            else
                            {
                                failure.accept(new CommandResultException("Channel " + channel.getAsMention() + " is already blacklisted."));
                            }
                        }
                        else
                        {
                            if (BlacklistUtils.removeChannel(channel, event.getGuild(), event.getMonke()))
                            {
                                event.replySuccess("Removed blacklist for channel " + channel.getAsMention());
                            }
                            else
                            {
                                failure.accept(new CommandResultException("Channel " + channel.getAsMention() + " is not blacklisted."));
                            }
                        }
                    }
                });
    }
}