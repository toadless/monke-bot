package net.toadless.monkebot.objects.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toadless.monkebot.Monke;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.pojos.Reminders;

import java.sql.Connection;
import java.time.LocalDateTime;

import static org.jooq.generated.tables.Reminders.REMINDERS;

public class Reminder
{
    private Reminder()
    {
        //Overrides the default, public, constructor
    }

    public static boolean remove(long userId, long guildId, Monke monke)
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var ctx = monke.getDatabaseHandler().getContext(connection);
            var existsQuery = ctx.selectFrom(Tables.REMINDERS)
                    .where(REMINDERS.USER_ID.eq(userId))
                    .and(REMINDERS.GUILD_ID.eq(guildId));
            if (existsQuery.fetch().isEmpty())
            {
                return false;
            }

            ctx.deleteFrom(Tables.REMINDERS)
                    .where(REMINDERS.USER_ID.eq(userId))
                    .and(REMINDERS.GUILD_ID.eq(guildId))
                    .execute();
            return true;
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
            return false;
        }
    }

    public static boolean add(Monke monke, long guildId, long userId, long channelId, String reminderText, LocalDateTime expiry)
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var ctx = monke.getDatabaseHandler().getContext(connection);

            boolean exists = ctx.select(REMINDERS.USER_ID).from(Tables.REMINDERS).fetchOne() != null;
            if (exists)
            {
                return false;
            }

            ctx.insertInto(Tables.REMINDERS).columns(REMINDERS.GUILD_ID, REMINDERS.USER_ID, REMINDERS.CHANNEL_ID, REMINDERS.REMINDER_TEXT, REMINDERS.EXPIRY).values(guildId, userId, channelId, reminderText, expiry).execute();

        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
            return false;
        }
        return true;
    }

    public static void remind(Monke monke, Reminders reminder)
    {
        remove(
                reminder.getUserId(),
                reminder.getGuildId(),
                monke
        );

        Guild guild = monke.getShardManager().getGuildById(reminder.getGuildId());
        Member member = guild.getMemberById(reminder.getUserId());
        TextChannel channel = guild.getTextChannelById(reminder.getChannelId());

        channel.sendMessage(member.getAsMention() + ", " + reminder.getReminderText()).queue();
    }

    public static Reminders getReminderById(long guildId, long userId, Monke monke)
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var ctx = monke.getDatabaseHandler().getContext(connection);
            var existsQuery = ctx.selectFrom(Tables.REMINDERS)
                    .where(REMINDERS.USER_ID.eq(userId))
                    .and(REMINDERS.GUILD_ID.eq(guildId));

            if (existsQuery.fetch().isEmpty())
            {
                return null;
            }
            var result = existsQuery.fetch().get(0);
            existsQuery.close();

            return new Reminders(result.getId(), result.getGuildId(), result.getUserId(), result.getChannelId(), result.getReminderText(), result.getExpiry());
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
            return null;
        }
    }
}