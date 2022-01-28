package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is a reference of what we pull from the database.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class Tempbans implements Serializable
{
    private Long userId;
    private Long guildId;
    private LocalDateTime mutedUntil;

    public Tempbans(Tempbans value)
    {
        this.userId = value.userId;
        this.guildId = value.guildId;
        this.mutedUntil = value.mutedUntil;
    }

    public Tempbans(
            Long userId,
            Long guildId,
            LocalDateTime mutedUntil
    )
    {
        this.userId = userId;
        this.guildId = guildId;
        this.mutedUntil = mutedUntil;
    }

    public Tempbans() {}

    public Long getUserId()
    {
        return this.userId;
    }

    public Long getGuildId()
    {
        return this.guildId;
    }

    public LocalDateTime getMutedUntil()
    {
        return this.mutedUntil;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public void setMutedUntil(LocalDateTime mutedUntil)
    {
        this.mutedUntil = mutedUntil;
    }
}