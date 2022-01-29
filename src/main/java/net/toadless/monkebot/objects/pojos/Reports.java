package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is a reference of what we pull from the database.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class Reports implements Serializable
{
    private Long messageId;
    private Long reportMessageId;
    private Long channelId;
    private Long guildId;
    private Long reporterId;
    private Long reportteeId;
    private LocalDateTime timestamp;
    private String reportText;

    public Reports(Reports value)
    {
        this.messageId = value.messageId;
        this.reportMessageId = value.reportMessageId;
        this.channelId = value.channelId;
        this.guildId = value.guildId;
        this.reporterId = value.reporterId;
        this.reportteeId = value.reportteeId;
        this.timestamp = value.timestamp;
        this.reportText = value.reportText;
    }

    public Reports(
            Long messageId,
            Long reportMessageId,
            Long channelId,
            Long guildId,
            Long reporterId,
            Long reportteeId,
            LocalDateTime timestamp,
            String reportText
    )
    {
        this.messageId = messageId;
        this.reportMessageId = reportMessageId;
        this.channelId = channelId;
        this.guildId = guildId;
        this.reporterId = reporterId;
        this.reportteeId = reportteeId;
        this.timestamp = timestamp;
        this.reportText = reportText;
    }

    public Reports() {}

    public Long getMessageId()
    {
        return messageId;
    }

    public Long getReportMessageId()
    {
        return reportMessageId;
    }

    public Long getChannelId()
    {
        return channelId;
    }

    public Long getGuildId()
    {
        return guildId;
    }

    public Long getReporterId()
    {
        return reporterId;
    }

    public Long getReportteeId()
    {
        return reportteeId;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public String getReportText()
    {
        return reportText;
    }

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public void setReportMessageId(Long reportMessageId)
    {
        this.reportMessageId = reportMessageId;
    }

    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public void setReporterId(Long reporterId)
    {
        this.reporterId = reporterId;
    }

    public void setReportteeId(Long reportteeId)
    {
        this.reportteeId = reportteeId;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setReportText(String reportText)
    {
        this.reportText = reportText;
    }
}