package net.toadless.monkebot.objects.exception;

import net.toadless.monkebot.objects.command.Command;

public class CommandCooldownException extends CommandException
{
    public CommandCooldownException(Command command)
    {
        super(command);
    }
}