package net.toadless.monkebot.objects.exception;


public class CommandResultException extends CommandException
{
    public CommandResultException(String text)
    {
        super(text);
    }
}