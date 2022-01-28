package net.toadless.monkebot.commands.maincommands.misc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class UptimeCommand extends Command
{
    public UptimeCommand()
    {
        super("Uptime", "Displays the bots uptime.", "[none]");
        addAliases("uptime");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        Duration uptime = Duration.between(event.getMonke().getStartTimestamp(), LocalDateTime.now());
        event.sendMessage(new EmbedBuilder()
                .setDescription(
                        "Uptime: " + uptime.toDaysPart() +
                                " days, " + uptime.toHoursPart() +
                                " hours, " + uptime.toSecondsPart() +
                                " seconds.")
                .setColor(Constants.EMBED_COLOUR));
    }
}