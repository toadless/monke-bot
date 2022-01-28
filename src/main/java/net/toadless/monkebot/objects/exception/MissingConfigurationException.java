package net.toadless.monkebot.objects.exception;

public class MissingConfigurationException extends CommandException
{
    public MissingConfigurationException(String text)
    {
        super(text);
    }
}