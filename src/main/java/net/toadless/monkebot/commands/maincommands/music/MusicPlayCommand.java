package net.toadless.monkebot.commands.maincommands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.toadless.monkebot.handlers.MusicHandler;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.objects.music.GuildMusicManager;
import net.toadless.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicPlayCommand extends Command
{
    public MusicPlayCommand()
    {
        super("Play", "Plays music from Youtube or Soundcloud.", "[song]");
        addAliases("play");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MusicHandler musicHandler = event.getMonke().getMusicHandler();
        GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

        if (CommandChecks.argsEmpty(event, failure)) return;
        if (CommandChecks.sharesVoice(event, failure)) return;
        if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;

        VoiceChannel channel = event.getMember().getVoiceState().getChannel(); //Safe due to CommandChecks
        String query = String.join("", args);
        addFlags(CommandFlag.GUILD_ONLY);

        if (!CommandChecks.isURL(query))
        {
            query = "ytsearch:" + query;
        }

        manager.bind(event.getChannel());
        musicHandler.getPlayerManager().loadItemOrdered(manager, query, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                if (manager.isPlaying())
                {
                    event.replySuccess("Added **" + track.getInfo().title + "** to the queue.");
                }
                manager.play(channel, track, event.getAuthor()); //Safe due to CommandChecks
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                if (playlist.isSearchResult())
                {
                    AudioTrack track = playlist.getTracks().get(0);
                    if (manager.isPlaying())
                    {
                        event.replySuccess("Added **" + track.getInfo().title + "** to the queue.");
                    }
                    manager.play(channel, track, event.getAuthor()); //Safe due to CommandChecks
                }
                else
                {
                    event.replySuccess("Added " + playlist.getTracks().size() + " tracks to the queue.");
                    manager.playAll(channel, playlist.getTracks(), event.getAuthor()); //Safe due to CommandChecks
                }

            }

            @Override
            public void noMatches()
            {
                failure.accept(new CommandResultException("Couldn't find anything matching your query."));
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                failure.accept(new CommandResultException("An error occurred while loading the song."));
            }
        });
    }
}
