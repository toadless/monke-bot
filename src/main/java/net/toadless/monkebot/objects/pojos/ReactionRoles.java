package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;

/**
 * This class is a reference of what we pull from the database.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class ReactionRoles implements Serializable
{
    private Long messageId;
    private Long guildId;
    private String emoteId;
    private Long roleId;

    public ReactionRoles(ReactionRoles value)
    {
        this.messageId = value.messageId;
        this.guildId = value.guildId;
        this.emoteId = value.emoteId;
        this.roleId = value.roleId;
    }

    public ReactionRoles(
            Long messageId,
            Long guildId,
            String emoteId,
            Long roleId
    )
    {
        this.messageId = messageId;
        this.guildId = guildId;
        this.emoteId = emoteId;
        this.roleId = roleId;
    }

    public ReactionRoles() {}

    public Long getMessageId()
    {
        return this.messageId;
    }

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public Long getGuildId()
    {
        return this.guildId;
    }

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public String getEmoteId()
    {
        return this.emoteId;
    }

    public void setEmoteId(String emoteId)
    {
        this.emoteId = emoteId;
    }

    public Long getRoleId()
    {
        return this.roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }
}