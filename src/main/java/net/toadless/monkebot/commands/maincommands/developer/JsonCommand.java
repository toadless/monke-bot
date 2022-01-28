package net.toadless.monkebot.commands.maincommands.developer;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class JsonCommand extends Command
{
    public JsonCommand()
    {
        super("Json", "Shows the JSON for a Discord message.", "[id]");
        addAliases("json", "getjson");
        addFlags(CommandFlag.DEVELOPER_ONLY, CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;

        JDA jda = event.getJDA();
        MessageChannel channel = event.getChannel();

        new RestActionImpl<>(jda, Route.Messages.GET_MESSAGE.compile(channel.getId(), args.get(0)),
                (response, request) ->
                {
                    String json = StringUtils.prettyPrintJSON(response.getObject().toString());

                    StringUtils.sendPartialMessages(json, event.getChannel());

                    return null;
                }).queue(null, error -> failure.accept(new CommandInputException("Message " + args.get(0) + " was not found in this channel.")));
    }
}