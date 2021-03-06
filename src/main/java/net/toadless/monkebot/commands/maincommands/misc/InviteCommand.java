package net.toadless.monkebot.commands.maincommands.misc;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class InviteCommand extends Command
{
    public InviteCommand()
    {
        super("Invite", "Shows the best invite for the server.", "[none]");
        addFlags(CommandFlag.GUILD_ONLY);
        addSelfPermissions(Permission.MANAGE_SERVER);
        addAliases("invite");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        Guild guild = event.getGuild();

        guild.retrieveInvites().queue(
                invites ->
                {
                    for (Invite invite : invites)
                    {
                        if (invite.getMaxUses() == 0)
                        {
                            event.replySuccess("Invite found: " + invite.getUrl());
                            return;
                        }
                    }
                    failure.accept(new CommandResultException("No invites found."));
                }
        );
    }
}