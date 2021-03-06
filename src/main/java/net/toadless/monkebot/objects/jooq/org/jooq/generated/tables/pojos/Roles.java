/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final Long userId;
    private final Long guildId;
    private final Long roleId;

    public Roles(Roles value) {
        this.id = value.id;
        this.userId = value.userId;
        this.guildId = value.guildId;
        this.roleId = value.roleId;
    }

    public Roles(
        Long id,
        Long userId,
        Long guildId,
        Long roleId
    ) {
        this.id = id;
        this.userId = userId;
        this.guildId = guildId;
        this.roleId = roleId;
    }

    /**
     * Getter for <code>public.roles.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.roles.user_id</code>.
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * Getter for <code>public.roles.guild_id</code>.
     */
    public Long getGuildId() {
        return this.guildId;
    }

    /**
     * Getter for <code>public.roles.role_id</code>.
     */
    public Long getRoleId() {
        return this.roleId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Roles (");

        sb.append(id);
        sb.append(", ").append(userId);
        sb.append(", ").append(guildId);
        sb.append(", ").append(roleId);

        sb.append(")");
        return sb.toString();
    }
}
