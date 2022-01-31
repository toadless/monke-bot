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

public class VoteConfigCommand extends Command
{
    public VoteConfigCommand(Command parent)
    {
        super(parent, "vote", "Gets and sets the vote channel for this server.", "<newChannel> / <none>");
        addFlags(CommandFlag.GUILD_ONLY);
        addMemberPermissions(Permission.MANAGE_SERVER);
        addAliases("vote", "votechannel", "votes", "voting");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MessageChannel channel = event.getChannel();
        GuildSettingsCache config = GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke());


        if (args.isEmpty())
        {
            EmbedUtils.sendDeletingEmbed(channel, new EmbedBuilder()
                    .setDescription("My vote channel for this server is `" + config.getVoteChannel() + "`.")
                    .setColor(Constants.EMBED_COLOUR), 30000);
            return;
        }

        new Parser(args.get(0), event).parseAsTextChannel(newChannel ->
        {
            config.setVoteChannel(newChannel.getIdLong());
            EmbedUtils.sendSuccess(channel, "Set the guilds vote channel to " + newChannel.getAsMention() + ".");
        });
    }
}