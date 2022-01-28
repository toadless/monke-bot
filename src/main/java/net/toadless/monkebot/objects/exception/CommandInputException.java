package net.toadless.monkebot.objects.exception;

public class CommandInputException extends CommandException
{
    public CommandInputException(String text)
    {
        super(text);
    }
}