package net.toadless.monkebot.commands.subcommands.info;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.info.BotInfo;
import org.jetbrains.annotations.NotNull;

public class BotInfoCommand extends Command
{
    public BotInfoCommand(Command parent)
    {
        super(parent, "bot", "Shows information about the bot.", "[none]");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        event.sendMessage(new EmbedBuilder()
                .setTitle(event.getJDA().getSelfUser().getName() + " information")
                .addField("JVM Version", BotInfo.getJavaVersion(), true)
                .addField("JDA Version", BotInfo.getJDAVersion(), true)
                .addField("Monke Version", Constants.VERSION, true)

                .addField("Thread Count", String.valueOf(BotInfo.getThreadCount()), true)
                .addField("Memory Usage", BotInfo.getMemoryFormatted() + " [" + BotInfo.getMemoryPercent() + "%]", true)
                .addField("CPU Usage", new DecimalFormat("#.##").format(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()) + "%", true)

                .addField("Shard Info", event.getJDA().getShardInfo().getShardString(), true)
                .addField("Server Count", String.valueOf(BotInfo.getGuildCount(event.getMonke().getShardManager())), true)
                .addField("Total Users", String.valueOf(event
                        .getMonke()
                        .getShardManager()
                        .getGuildCache()
                        .applyStream(guildStream -> guildStream.mapToInt(Guild::getMemberCount))
                        .sum()), true));
    }
}