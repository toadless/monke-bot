package net.toadless.monkebot.objects.database;

import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.pojos.Reports;
import org.bson.Document;

import java.time.LocalDateTime;

public class Report
{
    private static final String collection = "reports";

    private final long messageId;
    private final long commandMessageId;
    private final long channelId;
    private final long guildId;
    private final long reportedUserId;
    private final String reason;
    private final Monke monke;
    private final long reportingUserId;

    private Report(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, long reportingUserId, String reason, Monke monke)
    {
        this.messageId = messageId;
        this.commandMessageId = commandMessageId;
        this.channelId = channelId;
        this.guildId = guildId;
        this.reportedUserId = reportedUserId;
        this.reportingUserId = reportingUserId;
        this.reason = reason;
        this.monke = monke;
    }

    public static Report getById(long messageId, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Report.collection, Reports.class);
            var document = new Document("messageId", messageId);
            var result = collection.find(document);

            if (result.first() != null)
            {
                var report = result.first();
                return new Report(report.getMessageId(), report.getReportMessageId(), report.getChannelId(), report.getGuildId(), report.getReporterId(), report.getReportteeId(), report.getReportText(), monke);
            }
            else
            {
                return null;
            }
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
            return null;
        }
    }

    public static void add(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, long reportingUserId, String reason, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Report.collection, Reports.class);

            collection.insertOne(new Reports(
                    messageId,
                    commandMessageId,
                    channelId,
                    guildId,
                    reportingUserId,
                    reportedUserId,
                    LocalDateTime.now(),
                    reason
            ));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }


    public static void remove(long messageId, Monke monke)
    {
        try
        {
            var connection = monke.getDatabaseHandler().getConnection();
            var database = connection.getDatabase(monke.getDatabaseHandler().getDatabase().getName());
            var collection = database.getCollection(Report.collection, Reports.class);
            var document = new Document("messageId", messageId);

            collection.findOneAndDelete(document);
        }
        catch (Exception exception)
        {
            monke.getLogger().error("A mongo error occurred", exception);
        }
    }

    public long getReportingUserId()
    {
        return reportingUserId;
    }

    public long getMessageId()
    {
        return messageId;
    }

    public long getCommandMessageId()
    {
        return commandMessageId;
    }

    public long getChannelId()
    {
        return channelId;
    }

    public long getGuildId()
    {
        return guildId;
    }

    public long getReportedUserId()
    {
        return reportedUserId;
    }

    public String getReason()
    {
        return reason;
    }

    public Monke getMonke()
    {
        return monke;
    }
}
