package net.toadless.monkebot.commands.maincommands.music;

import java.util.List;
import java.util.function.Consumer;
import net.toadless.monkebot.handlers.MusicHandler;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.music.GuildMusicManager;
import net.toadless.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicLeaveCommand extends Command
{
    public MusicLeaveCommand()
    {
        super("Leave", "Makes the bot leave your VC.", "[none]");
        addAliases("leave");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MusicHandler musicHandler = event.getMonke().getMusicHandler();
        GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

        if (CommandChecks.sharesVoice(event, failure)) return;
        if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
        if (CommandChecks.inVoice(event, failure)) return;

        manager.getPlayer().destroy();
        manager.leave(event.getGuild());
        manager.getScheduler().clear();

        event.replySuccess("Bye!");
    }
}