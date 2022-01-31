/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.generated.tables.Guilds;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GuildsRecord extends UpdatableRecordImpl<GuildsRecord> implements Record7<Long, Long, Long, Long, Long, Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.guilds.guild_id</code>.
     */
    public GuildsRecord setGuildId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.guild_id</code>.
     */
    public Long getGuildId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.guilds.log_channel</code>.
     */
    public GuildsRecord setLogChannel(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.log_channel</code>.
     */
    public Long getLogChannel() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.guilds.muted_role</code>.
     */
    public GuildsRecord setMutedRole(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.muted_role</code>.
     */
    public Long getMutedRole() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.guilds.report_channel</code>.
     */
    public GuildsRecord setReportChannel(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.report_channel</code>.
     */
    public Long getReportChannel() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.guilds.vote_channel</code>.
     */
    public GuildsRecord setVoteChannel(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.vote_channel</code>.
     */
    public Long getVoteChannel() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.guilds.welcome_channel</code>.
     */
    public GuildsRecord setWelcomeChannel(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.welcome_channel</code>.
     */
    public Long getWelcomeChannel() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>public.guilds.prefix</code>.
     */
    public GuildsRecord setPrefix(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.prefix</code>.
     */
    public String getPrefix() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, Long, Long, Long, Long, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, Long, Long, Long, Long, Long, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Guilds.GUILDS.GUILD_ID;
    }

    @Override
    public Field<Long> field2() {
        return Guilds.GUILDS.LOG_CHANNEL;
    }

    @Override
    public Field<Long> field3() {
        return Guilds.GUILDS.MUTED_ROLE;
    }

    @Override
    public Field<Long> field4() {
        return Guilds.GUILDS.REPORT_CHANNEL;
    }

    @Override
    public Field<Long> field5() {
        return Guilds.GUILDS.VOTE_CHANNEL;
    }

    @Override
    public Field<Long> field6() {
        return Guilds.GUILDS.WELCOME_CHANNEL;
    }

    @Override
    public Field<String> field7() {
        return Guilds.GUILDS.PREFIX;
    }

    @Override
    public Long component1() {
        return getGuildId();
    }

    @Override
    public Long component2() {
        return getLogChannel();
    }

    @Override
    public Long component3() {
        return getMutedRole();
    }

    @Override
    public Long component4() {
        return getReportChannel();
    }

    @Override
    public Long component5() {
        return getVoteChannel();
    }

    @Override
    public Long component6() {
        return getWelcomeChannel();
    }

    @Override
    public String component7() {
        return getPrefix();
    }

    @Override
    public Long value1() {
        return getGuildId();
    }

    @Override
    public Long value2() {
        return getLogChannel();
    }

    @Override
    public Long value3() {
        return getMutedRole();
    }

    @Override
    public Long value4() {
        return getReportChannel();
    }

    @Override
    public Long value5() {
        return getVoteChannel();
    }

    @Override
    public Long value6() {
        return getWelcomeChannel();
    }

    @Override
    public String value7() {
        return getPrefix();
    }

    @Override
    public GuildsRecord value1(Long value) {
        setGuildId(value);
        return this;
    }

    @Override
    public GuildsRecord value2(Long value) {
        setLogChannel(value);
        return this;
    }

    @Override
    public GuildsRecord value3(Long value) {
        setMutedRole(value);
        return this;
    }

    @Override
    public GuildsRecord value4(Long value) {
        setReportChannel(value);
        return this;
    }

    @Override
    public GuildsRecord value5(Long value) {
        setVoteChannel(value);
        return this;
    }

    @Override
    public GuildsRecord value6(Long value) {
        setWelcomeChannel(value);
        return this;
    }

    @Override
    public GuildsRecord value7(String value) {
        setPrefix(value);
        return this;
    }

    @Override
    public GuildsRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, Long value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GuildsRecord
     */
    public GuildsRecord() {
        super(Guilds.GUILDS);
    }

    /**
     * Create a detached, initialised GuildsRecord
     */
    public GuildsRecord(Long guildId, Long logChannel, Long mutedRole, Long reportChannel, Long voteChannel, Long welcomeChannel, String prefix) {
        super(Guilds.GUILDS);

        setGuildId(guildId);
        setLogChannel(logChannel);
        setMutedRole(mutedRole);
        setReportChannel(reportChannel);
        setVoteChannel(voteChannel);
        setWelcomeChannel(welcomeChannel);
        setPrefix(prefix);
    }
}
