package net.toadless.monkebot.commands.maincommands.misc;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.toadless.monkebot.objects.command.Command;
import net.toadless.monkebot.objects.command.CommandEvent;
import net.toadless.monkebot.objects.exception.CommandException;
import net.toadless.monkebot.objects.exception.CommandResultException;
import net.toadless.monkebot.objects.json.WikipediaPage;
import net.toadless.monkebot.util.CommandChecks;
import net.toadless.monkebot.util.StringUtils;
import net.toadless.monkebot.util.WebUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class WikipediaCommand extends Command
{
    public WikipediaCommand()
    {
        super("Wikipedia", "Shows you a wikipedia page. (We CANNOT control the content of the pages provided)", "[pageName]");
        addAliases("wiki", "wikipedia");
    }

    @Override
    public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
    {
        if (CommandChecks.argsEmpty(event, failure)) return;

        String query = String.join("_", args);

        if (query.equalsIgnoreCase("cbt"))
        {
            query = "Cock_and_ball_torture";
        }

        WebUtils.getWikipediaPage(event, query, page ->
        {
            if (page.getType() != WikipediaPage.PageType.STANDARD)
            {
                failure.accept(new CommandResultException("Subject was too vague or not found."));
                return;
            }

            String thumbnail = page.getThumbnail();
            EmbedBuilder embed = new EmbedBuilder();

            if (thumbnail != null && event.getTextChannel().isNSFW())
            {
                embed.setImage(thumbnail);
            }

            String extract = page.getExtract();

            if (extract != null)
            {
                if (extract.length() > MessageEmbed.TEXT_MAX_LENGTH)
                {
                    extract = extract.substring(0, MessageEmbed.TEXT_MAX_LENGTH - 3) + "...";
                }
            }
            embed.setTitle(page.getTitle())
                    .setDescription(extract)
                    .setFooter("Page last edited at " + StringUtils.parseDateTime(page.getTimestamp()) + " | ");
            event.sendMessage(embed);
        }, failure);
    }
}