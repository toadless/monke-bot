package net.toadless.monkebot.commands.maincommands.developer;

import java.util.List;
import java.util.function.Consumer;

import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class ShutdownCommand extends Command
{
    public ShutdownCommand()
    {
        super("Shutdown", "Shuts the bot down gracefully.", "[none]");
        addFlags(CommandFlag.DEVELOPER_ONLY);
        addAliases("shutdown");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        event.getMonke().getDatabaseHandler().close();
        event.getMonke().getTaskHandler().close();
        event.getJDA().getGuilds().forEach(guild -> event.getMonke().getMusicHandler().getGuildMusicManager(guild).kill(guild));
        event.getJDA().shutdown();

        event.getMonke().getLogger().warn("-- Quack was shutdown using shutdown command.");
        event.getMonke().getLogger().warn("-- Issued by: " + event.getAuthor().getAsTag());
        if (event.isFromGuild())
        {
            event.getMonke().getLogger().warn("-- In guild: " + event.getGuild().getName());
        }
        else
        {
            event.getMonke().getLogger().warn("-- In guild: " + "Shutdown in DMs.");
        }
        System.exit(0);
    }
}