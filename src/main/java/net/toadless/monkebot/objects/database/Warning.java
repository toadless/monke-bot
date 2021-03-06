package net.toadless.monkebot.objects.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import net.toadless.monkebot.Monke;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.pojos.Warnings;

import static org.jooq.generated.tables.Warnings.WARNINGS;

public class Warning
{
    private final long userId;
    private final long guildId;
    private final Monke monke;

    public Warning(Guild guild, User user, Monke monke)
    {
        this.guildId = guild.getIdLong();
        this.userId = user.getIdLong();
        this.monke = monke;
    }

    public void add(String reason)
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            var query = context.insertInto(Tables.WARNINGS).columns(WARNINGS.GUILD_ID, WARNINGS.USER_ID, WARNINGS.WARN_TEXT).values(guildId, userId, reason);
            query.execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public void remove(long key)
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            context.deleteFrom(Tables.WARNINGS).where(WARNINGS.ID.eq(key)).execute();
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }
    }

    public List<Warnings> get()
    {
        List<Warnings> result = new ArrayList<>();
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection);
            var query = context.selectFrom(Tables.WARNINGS).where(WARNINGS.GUILD_ID.eq(guildId)).and(WARNINGS.USER_ID.eq(userId));

            for (var value : query.fetch())
            {
                result.add(new Warnings(value.getId(), value.getUserId(), value.getGuildId(), value.getTimestamp(), value.getWarnText()));
            }

        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
        }

        return result;
    }

    public Warnings getByWarnId(long warnId)
    {
        try (Connection connection = monke.getDatabaseHandler().getConnection())
        {
            var context = monke.getDatabaseHandler().getContext(connection)
                    .selectFrom(WARNINGS)
                    .where(WARNINGS.ID.eq(warnId));

            var result = context.fetch();
            context.close();
            if (!result.isEmpty())
            {
                var warn = result.get(0);
                return new Warnings(warn.getId(), warn.getUserId(), warn.getGuildId(), warn.getTimestamp(), warn.getWarnText());
            }
            else
            {
                return null;
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("An SQL error occurred", exception);
            return null;
        }
    }

    public boolean isPresent(long warnId)
    {
        return getByWarnId(warnId) != null;
    }
}