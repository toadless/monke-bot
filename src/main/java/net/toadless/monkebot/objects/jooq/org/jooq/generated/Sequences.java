/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated;


import org.jooq.Sequence;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * Convenience access to all sequences in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.channel_blacklists_id_seq</code>
     */
    public static final Sequence<Long> CHANNEL_BLACKLISTS_ID_SEQ = Internal.createSequence("channel_blacklists_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.reaction_roles_id_seq</code>
     */
    public static final Sequence<Long> REACTION_ROLES_ID_SEQ = Internal.createSequence("reaction_roles_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.reminders_id_seq</code>
     */
    public static final Sequence<Long> REMINDERS_ID_SEQ = Internal.createSequence("reminders_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.reports_id_seq</code>
     */
    public static final Sequence<Long> REPORTS_ID_SEQ = Internal.createSequence("reports_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.roles_id_seq</code>
     */
    public static final Sequence<Long> ROLES_ID_SEQ = Internal.createSequence("roles_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.tempbans_id_seq</code>
     */
    public static final Sequence<Long> TEMPBANS_ID_SEQ = Internal.createSequence("tempbans_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.votes_id_seq</code>
     */
    public static final Sequence<Long> VOTES_ID_SEQ = Internal.createSequence("votes_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.warnings_id_seq</code>
     */
    public static final Sequence<Long> WARNINGS_ID_SEQ = Internal.createSequence("warnings_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.word_blacklists_id_seq</code>
     */
    public static final Sequence<Long> WORD_BLACKLISTS_ID_SEQ = Internal.createSequence("word_blacklists_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);
}
