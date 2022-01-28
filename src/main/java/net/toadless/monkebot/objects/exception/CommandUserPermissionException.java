package net.toadless.monkebot.objects.exception;

import net.toadless.monkebot.objects.command.Command;

public class CommandUserPermissionException extends CommandException
{
    public CommandUserPermissionException(Command command)
    {
        super(command);
    }
}