package net.toadless.monkebot.commands.maincommands.music;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.toadless.monkebot.handlers.MusicHandler;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.music.GuildMusicManager;
import net.toadless.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicJoinCommand extends Command
{
    public MusicJoinCommand()
    {
        super("Join", "Makes the bot join your VC.", "[none]");
        addAliases("join");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MusicHandler musicHandler = event.getMonke().getMusicHandler();
        GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

        if (CommandChecks.sharesVoice(event, failure)) return;
        if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;

        VoiceChannel channel = event.getMember().getVoiceState().getChannel();
        manager.join(channel);
        event.replySuccess("Joined " + channel.getName());
        manager.bind(event.getChannel());
    }
}