package net.toadless.monkebot.commands.maincommands.fun;

import net.toadless.monkebot.commands.subcommands.reminder.ReminderAddCommand;
import net.toadless.monkebot.commands.subcommands.reminder.ReminderRemoveCommand;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ReminderCommand extends Command
{
    public ReminderCommand()
    {
        super("Reminder", "Remind yourself to do something!", "[add / remove]");
        addFlags(CommandFlag.GUILD_ONLY);
        addChildren(
                new ReminderAddCommand(this),
                new ReminderRemoveCommand(this)
        );
        addAliases("reminder", "remind");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        failure.accept(new CommandSyntaxException(event));
    }
}
