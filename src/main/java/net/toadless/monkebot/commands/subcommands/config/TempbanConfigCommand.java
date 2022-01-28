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

public class TempbanConfigCommand extends Command
{
    public TempbanConfigCommand(Command parent)
    {
        super(parent, "tempban", "Gets and sets the temp ban role for this server.", "<newRole> / <none>");
        addFlags(CommandFlag.GUILD_ONLY);
        addMemberPermissions(Permission.MANAGE_SERVER);
        addAliases("tempban", "tempbanrole", "mute", "mutedrole");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MessageChannel channel = event.getChannel();
        GuildSettingsCache config = GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke());


        if (args.isEmpty())
        {
            EmbedUtils.sendDeletingEmbed(channel, new EmbedBuilder()
                    .setDescription("My temp ban role for this server is `" + config.getTempBanRole() + "`.")
                    .setColor(Constants.EMBED_COLOUR), 30000);
            return;
        }

        new Parser(args.get(0), event).parseAsRole(newRole ->
        {
            config.setTempBanRole(newRole.getIdLong());
            EmbedUtils.sendSuccess(channel, "Set the guilds temp ban role to " + newRole.getAsMention() + ".");
        });
    }
}