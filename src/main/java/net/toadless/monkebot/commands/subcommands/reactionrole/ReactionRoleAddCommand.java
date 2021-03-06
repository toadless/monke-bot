package net.toadless.monkebot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.database.ReactionRole;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandHierarchyException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class ReactionRoleAddCommand extends Command
{
    public ReactionRoleAddCommand(Command parent)
    {
        super(parent, "add", "Adds a reaction role.", "[messageId][channel][role][emote]");
        addFlags(CommandFlag.GUILD_ONLY);
        addMemberPermissions(Permission.MANAGE_SERVER);
        addSelfPermissions(Permission.MESSAGE_ADD_REACTION);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsSizeSubceeds(event, 4, failure)) return;

        String emote;
        if (!event.getMessage().getEmotes().isEmpty())
        {
            if (event.getMessage().getEmotes().get(0).isAnimated())
            {
                failure.accept(new CommandInputException("Animated emotes are not allowed."));
                return;
            }
            emote = event.getMessage().getEmotes().get(0).getId();
        }
        else
        {
            emote = args.get(3);
        }

        OptionalLong messageId = new Parser(args.get(0), event).parseAsUnsignedLong();

        if (messageId.isPresent())
        {
            new Parser(args.get(1), event).parseAsTextChannel(
                    channel -> channel.retrieveMessageById(messageId.getAsLong()).queue(
                            message -> new Parser(args.get(2), event).parseAsRole(
                                    role ->
                                    {
                                        if (!event.getSelfMember().canInteract(role) || !event.getMember().canInteract(role))
                                        {
                                            failure.accept(new CommandHierarchyException(this));
                                            return;
                                        }

                                        message.addReaction(emote).queue(
                                                success ->
                                                {
                                                    new ReactionRole(message.getIdLong(), role.getIdLong(), event.getGuild().getIdLong(), emote, event.getMonke()).add();
                                                    event.replySuccess("Reaction role added.");
                                                },
                                                error -> failure.accept(new CommandInputException("I could not add reaction `" + event.getMessage().getEmotes().get(0).getName() + "`")));
                                    }),
                            error -> failure.accept(new CommandInputException("Message with ID " + messageId.getAsLong() + " not found in channel " + channel.getAsMention()))));
        }
    }
}