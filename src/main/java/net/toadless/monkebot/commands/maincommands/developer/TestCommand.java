package net.toadless.monkebot.commands.maincommands.developer;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class TestCommand extends Command
{
    public TestCommand()
    {
        super("Test", "Tests the bots basic functionality.", "[none]");
        addAliases("test");
        addFlags(CommandFlag.DEVELOPER_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        event.replySuccess("Success");
        event.replyError("Error");
        failure.accept(new CommandException("Exception"));
        event.sendDeletingMessage(new EmbedBuilder().setTitle("Test embed, now testing event waiting."));
        event.getMonke().getEventWaiter().waitForEvent(
                GuildMessageReceivedEvent.class,
                msg -> msg.getAuthor().equals(event.getAuthor()),
                msg -> event.replySuccess(msg.getMessage().getContentRaw()),
                5,
                TimeUnit.SECONDS,
                () -> event.replyError("Timeout"));
    }
}