package net.toadless.monkebot.commands.maincommands.fun;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.objects.json.RedditPost;
import net.toadless.monkebot.util.WebUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class QuackCommand extends Command
{
    private static final List<String> SUBREDDITS = List.of("duck");

    public QuackCommand()
    {
        super("Quack", "Shows a cute duck.", "[none]");
        addAliases("quack", "duck", "kwack");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        Random random = new Random();

        WebUtils.getPosts(event, SUBREDDITS.get(random.nextInt(SUBREDDITS.size())),
                data ->
                {
                    List<RedditPost> ducks = data
                            .stream()
                            .filter(post -> !post.isPinned() && !post.isStickied() && post.isMedia())
                            .collect(Collectors.toList());

                    if (ducks.isEmpty())
                    {
                        failure.accept(new CommandResultException("Couldn't find any ducks :pensive:"));
                        return;
                    }

                    RedditPost post = ducks.get(random.nextInt(ducks.size() - 1));

                    WebUtils.checkAndSendPost(event, post, failure);
                }, failure);
    }
}
