package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;

public class ChannelBlacklists implements Serializable
{
    private Long guildId;
    private Long channelId;

    public ChannelBlacklists(ChannelBlacklists value)
    {
        this.guildId = value.guildId;
        this.channelId = value.channelId;
    }

    public ChannelBlacklists(
            Long guildId,
            Long channelId
    )
    {
        this.guildId = guildId;
        this.channelId = channelId;
    }

    public ChannelBlacklists() {}

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getGuildId()
    {
        return guildId;
    }

    public Long getChannelId()
    {
        return channelId;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("ChannelBlacklists (");

        sb.append(guildId);
        sb.append(", ").append(channelId);

        sb.append(")");
        return sb.toString();
    }
}