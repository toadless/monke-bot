package net.toadless.monkebot.commands.subcommands.info;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.info.GuildInfo;
import net.toadless.monkebot.util.Parser;
import net.toadless.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ServerInfoCommand extends Command
{
    public ServerInfoCommand(Command parent)
    {
        super(parent, "server", "Shows information about a server", "[server]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        Optional<Guild> guild;
        if (args.isEmpty())
        {
            guild = Optional.of(event.getGuild());
        }
        else
        {
            guild = new Parser(args.get(0), event).parseAsGuild();
        }

        if (guild.isPresent())
        {
            GuildInfo guildInfo = new GuildInfo(guild.get());

            event.sendMessage(new EmbedBuilder()
                    .setTitle("Information for server: **" + guildInfo.getName() + "**")
                    .addField("Partner status", guildInfo.isPartner() ? "Is partnered" : "Not partnered", true)
                    .addField("Verified Status", guildInfo.isVerified() ? "Is verified" : "Not verified", true)
                    .addField("Public status", guildInfo.isPublic() ? "Is public" : "Not public", true)
                    .addField("Boost count", String.valueOf(guildInfo.getBoosts()), true)
                    .addField("Member count", guildInfo.getMemberCount() + " / " + guildInfo.getMaxMemberCount(), true)
                    .addField("Created at", StringUtils.parseDateTime(guildInfo.getTimeCreated()), true)
                    .setDescription(guildInfo.getDescription())
                    .setThumbnail(guildInfo.getIconURL()));
        }

    }
}