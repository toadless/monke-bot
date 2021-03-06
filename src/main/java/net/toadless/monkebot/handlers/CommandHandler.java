package net.toadless.monkebot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.toadless.monkebot.Constants;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.bot.ConfigOption;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.command.CommandFlag;
import net.toadless.monkebot.util.BlacklistUtils;
import net.toadless.monkebot.util.EmbedUtils;

public class CommandHandler
{
    public static final String COMMAND_PACKAGE = "net.toadless.monkebot.commands.maincommands";

    private final ClassGraph classGraph = new ClassGraph().acceptPackages(COMMAND_PACKAGE);
    private final Map<String, Command> commandMap;
    private final Monke monke;

    public CommandHandler(Monke monke)
    {
        this.monke = monke;
        commandMap = loadCommands();
    }

    public Map<String, Command> loadCommands()
    {
        Map<String, Command> commands = new LinkedHashMap<>();
        try (ScanResult result = classGraph.scan())
        {
            for (ClassInfo cls : result.getAllClasses())
            {
                Constructor<?>[] constructors = cls.loadClass().getDeclaredConstructors();
                if (constructors.length == 0)
                {
                    monke.getLogger().warn("No valid constructors found for Command class (" + cls.getSimpleName() + ")!");
                    continue;
                }
                if (constructors[0].getParameterCount() > 0)
                {
                    continue;
                }
                Object instance = constructors[0].newInstance();
                if (!(instance instanceof Command))
                {
                    monke.getLogger().warn("Non Command class (" + cls.getSimpleName() + ") found in commands package!");
                    continue;
                }
                Command cmd = (Command) instance;
                commands.put(cmd.getName(), cmd);
                for (String alias : cmd.getAliases()) commands.put(alias, cmd);
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A command exception occurred", exception);
            System.exit(1);
        }

        return commands;
    }

    public Map<String, Command> getCommandMap()
    {
        return commandMap;
    }

    public void handleEvent(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot() || event.isWebhookMessage())
        {
            return;
        }

        Message referencedMessage = event.getMessage().getReferencedMessage();

        if (referencedMessage != null && referencedMessage.getAuthor().equals(monke.getSelfUser()))
        {
            return;
        }

        if (event.isFromGuild())
        {
            if (!event.getTextChannel().canTalk())
            {
                return;
            }

            handleGuild(event);
        }
        else
        {
            handleDM(event);
        }
    }

    private void handleDM(MessageReceivedEvent event)
    {
        String prefix;
        String messageContent = event.getMessage().getContentRaw();

        if (isBotMention(event))
        {
            prefix = messageContent.substring(0, messageContent.indexOf(">"));
        }
        else
        {
            prefix = Constants.DEFAULT_BOT_PREFIX;
        }

        runCommand(prefix, messageContent, event);
    }


    private void handleGuild(MessageReceivedEvent event)
    {
        String prefix = GuildSettingsCache.getCache(event.getGuild().getIdLong(), monke).getPrefix();
        String messageContent = event.getMessage().getContentRaw();

        if (isBotMention(event))
        {
            prefix = messageContent.substring(0, messageContent.indexOf(">") + 1);
        }

        runCommand(prefix, messageContent, event);
    }

    private void runCommand(String prefix, String content, MessageReceivedEvent event)
    {
        boolean containsBlacklist = BlacklistUtils.isBlacklistedPhrase(event, monke);

        if (!content.startsWith(prefix))
        {
            if (containsBlacklist)
            {
                deleteBlacklisted(event);
            }
            return;
        }

        content = content.substring(prefix.length()); //Trim the prefix

        List<String> args = Arrays
                .stream(content.split("\\s+"))
                .filter(arg -> !arg.isBlank())
                .collect(Collectors.toList());

        if (args.isEmpty()) //No command was supplied, abort
        {
            return;
        }

        String command = args.get(0);
        if (command.isBlank() || command.startsWith(prefix)) //Empty string passed or double prefix supplied (eg ..)
        {
            return;
        }

        Command cmd = commandMap.get(command);

        if (cmd == null)
        {
            if (containsBlacklist)
            {
                deleteBlacklisted(event);
                return;
            }
            EmbedUtils.sendError(event.getChannel(), "Command `" + command + "` was not found.\n " +
                    "See " + prefix + "help for help.");
            return;
        }

        if (containsBlacklist && cmd.hasFlag(CommandFlag.BLACKLIST_BYPASS))
        {
            deleteBlacklisted(event);
            return;
        }

        args.remove(0); //Remove the command from the arguments
        CommandEvent commandEvent = new CommandEvent(event, monke, cmd, args);

        if (!cmd.hasChildren())
        {
            cmd.process(commandEvent);
            return;
        }

        if (args.isEmpty())
        {
            cmd.process(commandEvent);
            return;
        }

        cmd.getChildren()
                .stream()
                .filter(child -> child.getName().equalsIgnoreCase(args.get(0)))
                .findFirst()
                .ifPresentOrElse(
                        child -> child.process(new CommandEvent(event, monke, child, args.subList(1, args.size()))),
                        () -> cmd.process(commandEvent)); //Run any relevant child commands, or the main command if non are found
    }

    private void deleteBlacklisted(MessageReceivedEvent event)
    {
        EmbedUtils.sendError(event.getChannel(), "Your message contained a blacklisted phrase.");
        if (event.getGuild().getSelfMember().hasPermission((GuildChannel) event.getChannel(), Permission.MESSAGE_MANAGE))
        {
            event.getMessage().delete().queue();
        }
    }

    private boolean isBotMention(MessageReceivedEvent event)
    {
        String content = event.getMessage().getContentRaw();
        long id = event.getJDA().getSelfUser().getIdLong();
        return content.startsWith("<@" + id + ">") || content.startsWith("<@!" + id + ">");
    }
}