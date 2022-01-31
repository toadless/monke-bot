package net.toadless.monkebot.commands.subcommands.reminder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.database.Reminder;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import net.toadless.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ReminderAddCommand extends Command
{
    public ReminderAddCommand(Command parent)
    {
        super(parent, "add", "Creates a reminder.", "[time][text]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

        LocalDateTime expiry = new Parser(args.get(0), event).parseAsDuration();
        if (expiry == null || expiry.isAfter(LocalDateTime.now().plusWeeks(1)))
        {
            failure.accept(new CommandInputException("Expiry time " + args.get(1) + " is invalid."));
            return;
        }

        if (Reminder.getReminderById(event.getGuildIdLong(), event.getMember().getIdLong(), event.getMonke()) != null)
        {
            failure.accept(new CommandResultException("You already have a current reminder running."));
            return;
        }

        args.remove(0);
        String text = StringUtils.markdownSanitize(String.join(" ", args));

        if (Reminder.add(event.getMonke(), event.getGuildIdLong(), event.getMember().getIdLong(), event.getChannel().getIdLong(), text, expiry))
        {
            event.replySuccess("Created reminder for " + event.getMember().getAsMention());
        }
        else
        {
            failure.accept(new CommandResultException("Reminder was not created."));
        }
    }
}