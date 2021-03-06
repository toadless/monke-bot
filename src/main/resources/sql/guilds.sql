CREATE TABLE IF NOT EXISTS guilds
(
    guild_id BIGINT NOT NULL PRIMARY KEY,
    log_channel BIGINT NOT NULL DEFAULT -1,
    muted_role BIGINT NOT NULL DEFAULT -1,
    report_channel BIGINT NOT NULL DEFAULT -1,
    vote_channel BIGINT NOT NULL DEFAULT -1,
    welcome_channel BIGINT NOT NULL DEFAULT -1,
    prefix VARCHAR(5) NOT NULL DEFAULT 'm!'
);