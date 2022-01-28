package net.toadless.monkebot.commands.maincommands.misc;

import net.toadless.monkebot.commands.subcommands.reactionrole.ReactionRoleAddCommand;
import net.toadless.monkebot.commands.subcommands.reactionrole.ReactionRoleRemoveCommand;
import net.toadless.monkebot.commands.subcommands.reactionrole.ReactionRoleShowCommand;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings ("unused")
public class ReactionRoleCommand extends Command
{
    public ReactionRoleCommand()
    {
        super("Reaction Role", "Controls reaction roles.", "[add / remove / show]");
        addAliases("rr", "reactionrole");
        addFlags(CommandFlag.GUILD_ONLY);
        addChildren(
                new ReactionRoleAddCommand(this),
                new ReactionRoleRemoveCommand(this),
                new ReactionRoleShowCommand(this)
        );
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        failure.accept(new CommandSyntaxException(event));
    }
}