package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is a reference of what we pull from the database.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class Warnings implements Serializable
{
    private Long id;
    private Long guildId;
    private Long userId;
    private LocalDateTime timestamp;
    private String warnText;

    public Warnings(Warnings value)
    {
        this.id = value.id;
        this.guildId = value.guildId;
        this.userId = value.userId;
        this.timestamp = value.timestamp;
        this.warnText = value.warnText;
    }

    public Warnings(
            Long id,
            Long guildId,
            Long userId,
            LocalDateTime timestamp,
            String warnText
    )
    {
        this.id = id;
        this.guildId = guildId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.warnText = warnText;
    }

    public Warnings() {}

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setWarnText(String warnText)
    {
        this.warnText = warnText;
    }

    public Long getId()
    {
        return id;
    }

    public Long getGuildId()
    {
        return guildId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public String getWarnText()
    {
        return warnText;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Warnings (");

        sb.append(id);
        sb.append(", ").append(guildId);
        sb.append(", ").append(userId);
        sb.append(", ").append(timestamp);
        sb.append(", ").append(warnText);

        sb.append(")");
        return sb.toString();
    }
}