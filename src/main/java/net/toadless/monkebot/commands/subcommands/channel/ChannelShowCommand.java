package net.toadless.monkebot.commands.subcommands.channel;

import java.util.List;
import java.util.function.Consumer;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.util.BlacklistUtils;
import net.toadless.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jooq.generated.tables.pojos.ChannelBlacklists;

public class ChannelShowCommand extends Command
{
    public ChannelShowCommand(Command parent)
    {
        super(parent, "show", "Shows all configured channels for this server.", "[none]");
        addMemberPermissions(Permission.MANAGE_SERVER);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        StringBuilder text = new StringBuilder();
        for (ChannelBlacklists channel : BlacklistUtils.getBlacklistedChannels(event.getGuild(), event.getMonke()))
        {
            text.append(StringUtils.getChannelAsMention(channel.getChannelId())).append(" is blacklisted.");
        }

        event.sendMessage(new EmbedBuilder()
                .setTitle("Configured channels for " + event.getGuild().getName())
                .addField("Blacklisted Channels", text.length() == 0 ? "No blacklisted channels" : text.toString(), false));
    }
}