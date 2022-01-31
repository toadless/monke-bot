package net.toadless.monkebot.util;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.toadless.monkebot.Monke;
import net.dv8tion.jda.api.entities.Guild;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.Guilds;
import org.jooq.generated.tables.pojos.Reminders;
import org.jooq.generated.tables.pojos.Tempbans;
import org.jooq.generated.tables.pojos.Votes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

    private DatabaseUtils()
    {
        //Overrides the default, public, constructor
    }

    public static void removeGuild(Guild guild, Monke monke)
    {
        LOGGER.debug("Removed guild " + guild.getId());
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection)
                    .deleteFrom(Tables.GUILDS)
                    .where(Guilds.GUILDS.GUILD_ID.eq(guild.getIdLong()));
            context.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public static void removeGuild(long guildId, Monke monke)
    {
        LOGGER.debug("Removed guild " + guildId);
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection)
                    .deleteFrom(Tables.GUILDS)
                    .where(Guilds.GUILDS.GUILD_ID.eq(guildId));
            context.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public static void registerGuild(Guild guild, Monke monke)
    {
        LOGGER.debug("Registered guild " + guild.getId());
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection)
                    .insertInto(Tables.GUILDS)
                    .columns(Guilds.GUILDS.GUILD_ID)
                    .values(guild.getIdLong())
                    .onDuplicateKeyIgnore();
            context.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public static void registerGuild(long guildId, Monke monke)
    {
        LOGGER.debug("Removed guild " + guildId);
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection)
                    .insertInto(Tables.GUILDS)
                    .columns(Guilds.GUILDS.GUILD_ID)
                    .values(guildId)
                    .onDuplicateKeyIgnore();
            context.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public static List<Tempbans> getExpiredTempbans(Monke monke)
    {
        List<Tempbans> result = new ArrayList<>();
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection).selectFrom(Tables.TEMPBANS);

            for (var value : context.fetch())
            {
                if (value.getMutedUntil().isBefore(LocalDateTime.now()))
                {
                    result.add(new Tempbans(value.getId(), value.getUserId(), value.getGuildId(), value.getMutedUntil()));
                }
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
        return result;
    }


    public static List<Votes> getExpiredVotes(Monke monke)
    {
        List<Votes> result = new ArrayList<>();
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection).selectFrom(Tables.VOTES);

            for (var value : context.fetch())
            {
                if (value.getExpiry().isBefore(LocalDateTime.now()))
                {
                    result.add(new Votes(value.getId(), value.getVoteId(), value.getGuildId(), value.getDirectMessageId(), value.getUserId(), value.getOption(), value.getMaxOptions(), value.getExpiry(), value.getHasVoted()));
                }
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
        return result;
    }

    public static List<Reminders> getExpiredReminders(Monke monke)
    {
        List<Reminders> result = new ArrayList<>();
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection).selectFrom(Tables.REMINDERS);

            for (var value : context.fetch())
            {
                if (value.getExpiry().isBefore(LocalDateTime.now()))
                {
                    result.add(new Reminders(value.getId(), value.getGuildId(), value.getUserId(),value.getChannelId(), value.getReminderText(), value.getExpiry()));
                }
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
        return result;
    }
}