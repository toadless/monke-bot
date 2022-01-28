package net.toadless.monkebot.commands.maincommands.misc;

import net.toadless.monkebot.commands.subcommands.config.LogConfigCommand;
import net.toadless.monkebot.commands.subcommands.config.PrefixConfigCommand;
import net.toadless.monkebot.commands.subcommands.config.TempbanConfigCommand;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ConfigCommand extends Command
{
    public ConfigCommand()
    {
        super("Config", "Gets and sets the config for this server.", "[prefix/tempban/log]");
        addAliases("config", "configuration");
        addChildren(
                new PrefixConfigCommand(this),
                new LogConfigCommand(this),
                new TempbanConfigCommand(this)
        );
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        this.getChildren().get(0).run(args, event, failure);
    }
}