package net.toadless.monkebot.commands.subcommands.blacklist;

import java.util.List;
import java.util.function.Consumer;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.util.BlacklistUtils;
import net.toadless.monkebot.util.CommandChecks;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class BlacklistRemoveCommand extends Command
{
    public BlacklistRemoveCommand(Command parent)
    {
        super(parent, "remove", "Removes a phrase from the blacklist.", "[phrase]");
        addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE, CommandFlag.BLACKLIST_BYPASS);
        addMemberPermissions(Permission.MESSAGE_MANAGE);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;
        String phrase = String.join(" ", args);
        if (BlacklistUtils.removePhrase(event.getGuild(), phrase, event.getMonke()))
        {
            event.replySuccess("Removed phrase ||" + phrase + "|| from the blacklist");
        }
        else
        {
            failure.accept(new CommandResultException("Phrase ||" + phrase + "|| not found in the blacklist."));
        }
    }
}