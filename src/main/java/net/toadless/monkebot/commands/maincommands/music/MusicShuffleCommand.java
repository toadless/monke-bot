package net.toadless.monkebot.commands.maincommands.music;

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
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicShuffleCommand extends Command
{
    public MusicShuffleCommand()
    {
        super("Shuffle", "Shuffles the queue.", "[none]");
        addAliases("shuffle");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MusicHandler musicHandler = event.getMonke().getMusicHandler();
        GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

        if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
        if (CommandChecks.sharesVoice(event, failure)) return;

        if (manager.getScheduler().hasNext())
        {
            manager.getScheduler().shuffle();
            event.replySuccess("Shuffled the queue.");
        }
        else
        {
            failure.accept(new CommandResultException("No more tracks queued."));
        }
    }
}