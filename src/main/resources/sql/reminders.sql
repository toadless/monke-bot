CREATE TABLE IF NOT EXISTS reminders
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    reminder_text TEXT NOT NULL,
    expiry TIMESTAMP NOT NULL
);