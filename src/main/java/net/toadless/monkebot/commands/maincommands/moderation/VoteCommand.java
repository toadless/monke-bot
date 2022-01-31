package net.toadless.monkebot.commands.maincommands.moderation;

import java.util.List;
import java.util.function.Consumer;
import net.toadless.monkebot.commands.subcommands.vote.VoteCloseCommand;
import net.toadless.monkebot.commands.subcommands.vote.VoteCreateCommand;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandSyntaxException;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class VoteCommand extends Command
{
    public VoteCommand()
    {
        super("Vote", "Controls voting", "[create / close]");
        addAliases("vote");
        addMemberPermissions(Permission.MANAGE_SERVER);
        addChildren(
                new VoteCreateCommand(this),
                new VoteCloseCommand(this)
        );
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        failure.accept(new CommandSyntaxException(event));
    }
}