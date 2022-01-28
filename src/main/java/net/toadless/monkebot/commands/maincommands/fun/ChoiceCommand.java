package net.toadless.monkebot.commands.maincommands.fun;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import net.toadless.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class ChoiceCommand extends Command
{
    public ChoiceCommand()
    {
        super("Choice", "Chooses a 50/50 for you.", "[opt1] / [opt2]");
        addAliases("choice");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;

        List<String> slashArgs = new Parser(StringUtils.markdownSanitize(args.get(0)), event).parseAsSlashArgs();

        if (CommandChecks.argsSizeSubceeds(slashArgs, event, 2, failure)) return;
        Random random = new Random();
        int chance = random.nextInt(2);
        String choice;

        if (chance == 1)
        {
            choice = slashArgs.get(0);
        }
        else
        {
            choice = slashArgs.get(1);
        }

        event.getChannel()
                .sendMessage("I choose " + choice)
                .allowedMentions(Collections.emptyList())
                .queue();
    }
}
