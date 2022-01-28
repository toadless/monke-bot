package net.toadless.monkebot.commands.maincommands.music;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.toadless.monkebot.handlers.MusicHandler;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.objects.music.GuildMusicManager;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicVolumeCommand extends Command
{
    public MusicVolumeCommand()
    {
        super("Volume", "Sets the music volume", "<volume {100}>");
        addAliases("volume", "vol");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        MusicHandler musicHandler = event.getMonke().getMusicHandler();
        GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

        if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
        if (CommandChecks.sharesVoice(event, failure)) return;

        if (args.isEmpty())
        {
            event.replySuccess("The volume is " + manager.getPlayer().getVolume() + "%");
            return;
        }

        OptionalInt volume = new Parser(args.get(0), event).parseAsUnsignedInt();

        if (volume.isPresent())
        {
            if (volume.getAsInt() > 100)
            {
                failure.accept(new CommandInputException("Volume must be 100 or lower."));
                return;
            }

            manager.setVolume(volume.getAsInt());
            event.replySuccess("Set the volume to " + volume.getAsInt() + "%");
        }
    }
}
