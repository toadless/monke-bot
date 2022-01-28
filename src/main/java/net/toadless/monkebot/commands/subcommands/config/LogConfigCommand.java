package net.toadless.monkebot.commands.subcommands.config;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.util.EmbedUtils;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class LogConfigCommand extends Command
{
    public LogConfigCommand(Command parent)
    {
        super(parent, "log", "Gets and sets the log channel for this server.", "<newChannel> / <none>");
        addFlags(CommandFlag.GUILD_ONLY);
        addMemberPermissions(Permission.MANAGE_SERVER);
        addAliases("log", "logchannel", "logs", "logging");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MessageChannel channel = event.getChannel();
        GuildSettingsCache config = GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke());


        if (args.isEmpty())
        {
            EmbedUtils.sendDeletingEmbed(channel, new EmbedBuilder()
                    .setDescription("My log channel for this server is `" + config.getLogChannel() + "`.")
                    .setColor(Constants.EMBED_COLOUR), 30000);
            return;
        }

        new Parser(args.get(0), event).parseAsTextChannel(newChannel ->
        {
            config.setLogChannel(newChannel.getIdLong());
            EmbedUtils.sendSuccess(channel, "Set the guilds log channel to " + newChannel.getAsMention() + ".");
        });
    }
}