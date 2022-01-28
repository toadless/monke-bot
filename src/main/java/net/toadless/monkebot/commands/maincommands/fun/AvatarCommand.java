package net.toadless.monkebot.commands.maincommands.fun;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class AvatarCommand extends Command
{
    public AvatarCommand()
    {
        super("Avatar", "Shows the avatar for the a user.", "<user>");
        addAliases("avatar", "avi", "pfp");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (args.isEmpty())
        {
            event.sendMessage(new EmbedBuilder()
                    .setTitle(event.getAuthor().getAsTag() + "'s Avatar")
                    .setImage(event.getAuthor().getAvatarUrl() + "?size=2048"));
            return;
        }
        new Parser(args.get(0), event).parseAsUser(user ->
                event.sendMessage(new EmbedBuilder()
                        .setTitle(user.getAsTag() + "'s Avatar")
                        .setImage(user.getAvatarUrl() + "?size=2048")));
    }
}