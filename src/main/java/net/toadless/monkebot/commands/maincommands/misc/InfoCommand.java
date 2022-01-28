package net.toadless.monkebot.commands.maincommands.misc;

import java.util.List;
import java.util.function.Consumer;

import net.toadless.monkebot.commands.subcommands.info.BotInfoCommand;
import net.toadless.monkebot.commands.subcommands.info.RoleInfoCommand;
import net.toadless.monkebot.commands.subcommands.info.ServerInfoCommand;
import net.toadless.monkebot.commands.subcommands.info.UserInfoCommand;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class InfoCommand extends Command
{
    public InfoCommand()
    {
        super("Info", "Provides information about things.", "[user/server/bot/role]");
        addAliases("info");
        addFlags(CommandFlag.GUILD_ONLY);
        addChildren(
                new UserInfoCommand(this),
                new BotInfoCommand(this),
                new RoleInfoCommand(this),
                new ServerInfoCommand(this));
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        this.getChildren().get(0).run(args, event, failure);
    }
}
