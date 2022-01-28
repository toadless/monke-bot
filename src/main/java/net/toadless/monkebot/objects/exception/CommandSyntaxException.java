package net.toadless.monkebot.objects.exception;

import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;

public class CommandSyntaxException extends CommandException
{
    public CommandSyntaxException(Command command)
    {
        super(command);
    }

    public CommandSyntaxException(CommandEvent ctx)
    {
        super(ctx.getCommand());
    }
}