package net.toadless.monkebot.commands.subcommands.reminder;

import java.util.List;
import java.util.function.Consumer;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.database.Reminder;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import org.jetbrains.annotations.NotNull;

public class ReminderRemoveCommand extends Command
{
    public ReminderRemoveCommand(Command parent)
    {
        super(parent, "remove", "Removes a reminder.", "[none]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (Reminder.remove(event.getMember().getIdLong(), event.getGuildIdLong(), event.getMonke()))
        {
            event.replySuccess("Removed reminder for " + event.getMember().getAsMention());
        }
        else
        {
            failure.accept(new CommandResultException("You have no active reminder."));
        }
    }
}