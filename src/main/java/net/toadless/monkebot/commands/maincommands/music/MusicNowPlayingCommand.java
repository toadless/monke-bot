package net.toadless.monkebot.commands.maincommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import net.toadless.monkebot.handlers.MusicHandler;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.objects.music.GuildMusicManager;
import net.toadless.monkebot.util.CommandChecks;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.toadless.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicNowPlayingCommand extends Command
{
    public MusicNowPlayingCommand()
    {
        super("Now Playing", "Shows whats playing currently.", "[none]");
        addAliases("nowplaying", "np");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MusicHandler musicHandler = event.getMonke().getMusicHandler();
        GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

        if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
        if (CommandChecks.sharesVoice(event, failure)) return;

        AudioTrack currentTrack = manager.getPlayer().getPlayingTrack();

        if (currentTrack == null)
        {
            failure.accept(new CommandResultException("Nothing is playing."));
            return;
        }
        Duration length = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(currentTrack.getDuration() / 1000));
        Duration passed = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(currentTrack.getPosition() / 1000));

        event.sendMessage(new EmbedBuilder()
                .setTitle("Now playing for " + event.getGuild().getName())
                .setDescription(
                        "[" + currentTrack.getInfo().title + "](" + currentTrack.getInfo().uri + ")" +
                                "\n**Author**: " + currentTrack.getInfo().author +
                                "\n**Position**: " + StringUtils.parseDuration(passed) +
                                "\n**Length**: " + StringUtils.parseDuration(length) +
                                "\n**Requested by**: " + currentTrack.getUserData(User.class).getAsMention()));
    }
}