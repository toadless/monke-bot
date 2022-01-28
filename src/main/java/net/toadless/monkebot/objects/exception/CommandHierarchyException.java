package net.toadless.monkebot.objects.exception;

import net.toadless.monkebot.objects.command.Command;

public class CommandHierarchyException extends CommandException
{
    public CommandHierarchyException(Command command)
    {
        super(command);
    }
}