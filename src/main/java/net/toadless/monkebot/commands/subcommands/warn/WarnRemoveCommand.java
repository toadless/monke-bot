package net.toadless.monkebot.commands.subcommands.warn;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.database.Warning;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandHierarchyException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.CommandUtils;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class WarnRemoveCommand extends Command
{
    public WarnRemoveCommand(Command parent)
    {
        super(parent, "remove", "Removes a warning.", "[user][warning-id]");
        addMemberPermissions(Permission.MESSAGE_MANAGE);
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

        User author = event.getAuthor();
        Guild guild = event.getGuild();
        new Parser(args.get(0), event).parseAsUser(user ->
                {
                    if (user.isBot())
                    {
                        failure.accept(new CommandInputException("Bots cannot have warnings."));
                        return;
                    }

                    if (user.equals(author))
                    {
                        failure.accept(new CommandHierarchyException(this));
                        return;
                    }

                    CommandUtils.interactionCheck(author, user, event, () ->
                    {
                        OptionalInt warningNumber = new Parser(args.get(1), event).parseAsUnsignedInt();
                        if (warningNumber.isPresent())
                        {
                            Warning warning = new Warning(event.getGuild(), user, event.getMonke());

                            if (warning.get()<1)
                            {
                                failure.accept(new CommandInputException("Invalid warning specified."));
                                return;
                            }

                            new Warning(guild, user, event.getMonke()).remove(warningNumber.getAsInt());
                            event.replySuccess("Removed " + warningNumber.getAsInt() + " warning(s).");
                        }
                    });
                }
        );
    }
}