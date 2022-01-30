/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.generated.tables.ChannelBlacklists;
import org.jooq.generated.tables.Guilds;
import org.jooq.generated.tables.ReactionRoles;
import org.jooq.generated.tables.Reports;
import org.jooq.generated.tables.Roles;
import org.jooq.generated.tables.Tempbans;
import org.jooq.generated.tables.Warnings;
import org.jooq.generated.tables.WordBlacklists;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.channel_blacklists</code>.
     */
    public final ChannelBlacklists CHANNEL_BLACKLISTS = ChannelBlacklists.CHANNEL_BLACKLISTS;

    /**
     * The table <code>public.guilds</code>.
     */
    public final Guilds GUILDS = Guilds.GUILDS;

    /**
     * The table <code>public.reaction_roles</code>.
     */
    public final ReactionRoles REACTION_ROLES = ReactionRoles.REACTION_ROLES;

    /**
     * The table <code>public.reports</code>.
     */
    public final Reports REPORTS = Reports.REPORTS;

    /**
     * The table <code>public.roles</code>.
     */
    public final Roles ROLES = Roles.ROLES;

    /**
     * The table <code>public.tempbans</code>.
     */
    public final Tempbans TEMPBANS = Tempbans.TEMPBANS;

    /**
     * The table <code>public.warnings</code>.
     */
    public final Warnings WARNINGS = Warnings.WARNINGS;

    /**
     * The table <code>public.word_blacklists</code>.
     */
    public final WordBlacklists WORD_BLACKLISTS = WordBlacklists.WORD_BLACKLISTS;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Arrays.<Sequence<?>>asList(
            Sequences.CHANNEL_BLACKLISTS_ID_SEQ,
            Sequences.REACTION_ROLES_ID_SEQ,
            Sequences.REPORTS_ID_SEQ,
            Sequences.ROLES_ID_SEQ,
            Sequences.TEMPBANS_ID_SEQ,
            Sequences.WARNINGS_ID_SEQ,
            Sequences.WORD_BLACKLISTS_ID_SEQ);
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            ChannelBlacklists.CHANNEL_BLACKLISTS,
            Guilds.GUILDS,
            ReactionRoles.REACTION_ROLES,
            Reports.REPORTS,
            Roles.ROLES,
            Tempbans.TEMPBANS,
            Warnings.WARNINGS,
            WordBlacklists.WORD_BLACKLISTS);
    }
}
