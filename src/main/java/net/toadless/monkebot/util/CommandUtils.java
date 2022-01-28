package net.toadless.monkebot.util;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.command.CommandEvent;

public class CommandUtils
{
    private CommandUtils()
    {
        // Override the default, public, constructor
    }

    public static boolean isValidCommand(String message, long guildId, Monke monke)
    {
        return message.startsWith(GuildSettingsCache.getCache(guildId, monke).getPrefix()) || message.startsWith("<@" + monke.getSelfUser().getId() + ">") || message.startsWith("<@!" + monke.getSelfUser().getId() + ">");
    }

    public static void interactionCheck(User user1, User user2, CommandEvent ctx, Runnable onSuccess)
    {
        List<RestAction<?>> actions = new ArrayList<>();
        actions.add(ctx.getGuild().retrieveMember(user1));
        actions.add(ctx.getGuild().retrieveMember(user2));
        RestAction.allOf(actions).queue(results ->
                {
                    Member member1 = (Member) results.get(0);
                    Member member2 = (Member) results.get(1);

                    if (member1.canInteract(member2))
                    {
                        onSuccess.run();
                    }
                    else
                    {
                        ctx.replyError("A hierarchy issue occurred when trying to execute command `" + ctx.getCommand().getName() + "`");
                    }
                }
        );
    }
}