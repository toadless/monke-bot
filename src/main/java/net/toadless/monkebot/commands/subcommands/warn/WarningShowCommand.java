package net.toadless.monkebot.commands.subcommands.warn;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.database.Warning;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class WarningShowCommand extends Command
{
    public WarningShowCommand(Command parent)
    {
        super(parent, "show", "Shows a user's warnings.", "[user]");
        addAliases("list");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;

        new Parser(args.get(0), event).parseAsUser(user ->
        {
            if (user.isBot())
            {
                failure.accept(new CommandInputException("Bots cannot have warnings."));
                return;
            }

            Guild guild = event.getGuild();
            MessageChannel channel = event.getChannel();
            Warning warning = new Warning(guild, user, event.getMonke());

            channel.sendMessage(new EmbedBuilder()
                    .setTitle("Warnings for " + user.getAsTag())
                    .setDescription(warning.get() == 0 ? "This user has no warnings" : warning.get() + " warning(s).")
                    .setColor(Constants.EMBED_COLOUR)
                    .build()).queue();
        });
    }
}