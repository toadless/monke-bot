package net.toadless.monkebot.commands.maincommands.moderation;

import net.dv8tion.jda.api.Permission;
import net.toadless.monkebot.commands.subcommands.blacklist.BlacklistAddCommand;
import net.toadless.monkebot.commands.subcommands.blacklist.BlacklistRemoveCommand;
import net.toadless.monkebot.commands.subcommands.blacklist.BlacklistShowCommand;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings ("unused")
public class BlacklistCommand extends Command
{
    public BlacklistCommand()
    {
        super("Blacklist", "Controls the blacklists.", "[add / remove / show]");
        addAliases("blacklist");
        addMemberPermissions(Permission.MANAGE_SERVER);
        addChildren(
                new BlacklistAddCommand(this),
                new BlacklistRemoveCommand(this),
                new BlacklistShowCommand(this)
        );
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        failure.accept(new CommandSyntaxException(event));
    }
}