package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;

/**
 * This class is a reference of what we pull from the database.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class Warnings implements Serializable
{
    private Long guildId;
    private Long userId;
    private int amount;

    public Warnings(Warnings value)
    {
        this.guildId = value.guildId;
        this.userId = value.userId;
        this.amount = value.amount;
    }

    public Warnings(
            Long guildId,
            Long userId,
            int amount
    )
    {
        this.guildId = guildId;
        this.userId = userId;
        this.amount = amount;
    }

    public Warnings() {}

    public Long getGuildId()
    {
        return this.guildId;
    }

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public Long getUserId()
    {
        return this.userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
}