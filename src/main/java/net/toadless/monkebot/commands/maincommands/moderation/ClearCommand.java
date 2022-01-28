package net.toadless.monkebot.commands.maincommands.moderation;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.toadless.monkebot.handlers.CooldownHandler;
import net.toadless.monkebot.objects.cache.CachedMessage;
import net.toadless.monkebot.objects.cache.MessageCache;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandCooldownException;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandInputException;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class ClearCommand extends Command
{
    public ClearCommand()
    {
        super("Clear", "Clears messages from the current channel.", "[amount {50}]");
        addAliases("clear", "purge");
        addMemberPermissions(Permission.MESSAGE_MANAGE);
        addSelfPermissions(Permission.MESSAGE_MANAGE);
        setCooldown(10000L);
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;

        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        OptionalInt amount = new Parser(args.get(0), event).parseAsUnsignedInt();

        if (amount.isPresent())
        {
            if (amount.getAsInt() > 50)
            {
                failure.accept(new CommandInputException("Enter an amount less than or equal to 50."));
                return;
            }

            if (CooldownHandler.isOnCooldown(member, this))
            {
                failure.accept(new CommandCooldownException(this));
                return;
            }

            channel.getIterableHistory()
                    .takeAsync(amount.getAsInt() + 1)
                    .thenAccept(messages ->
                    {
                        MessageCache cache = MessageCache.getCache(guild.getIdLong());
                        messages.stream()
                                .map(CachedMessage::new)
                                .filter(message -> cache.isCached(message.getKey()))
                                .forEach(cache::remove);

                        CooldownHandler.addCooldown(member, this);
                        channel.purgeMessages(messages);
                        event.replySuccess("Deleted " + (messages.size() - 1) + " messages.");
                    });
        }
    }
}