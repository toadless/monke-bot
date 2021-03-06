package net.toadless.monkebot.commands.maincommands.fun;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.IOUtils;
import net.toadless.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MockCommand extends Command
{
    public MockCommand()
    {
        super("Mock", "Mocks the specified text.", "[text]");
        addAliases("mock");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;
        if (CommandChecks.argsEmbedCompatible(event, failure)) return;

        InputStream file = IOUtils.getResourceFile("mock.jpg");
        if (file == null)
        {
            failure.accept(new CommandResultException("An error occurred while loading the mock image."));
            return;
        }

        event.getChannel().sendFile(file, "mock.jpg").embed(new EmbedBuilder()
                .setTitle(mockText(args))
                .setColor(Constants.EMBED_COLOUR)
                .setImage("attachment://mock.jpg")
                .setTimestamp(Instant.now())
                .build()).queue();

    }

    private String mockText(List<String> args)
    {
        StringBuilder mockText = new StringBuilder();
        Random random = new Random();
        mockText.append('"');
        args.forEach(word ->
        {
            for (String selectedChar : StringUtils.markdownSanitize(word).split(""))
            {
                mockText.append(random.nextBoolean() ? selectedChar.toUpperCase() : selectedChar.toLowerCase());
            }
            mockText.append(" ");
        });
        mockText.deleteCharAt(mockText.lastIndexOf(" "));
        mockText.append('"');
        return mockText.toString();
    }
}