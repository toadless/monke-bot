package net.toadless.monkebot.objects.bot;

public enum ConfigOption
{
    TOKEN("token", "token"),
    PRIVILEGEDUSERS("privilegedusers", "0000000000000, 0000000000000"),
    LOG_CHANNEL("logchannel", "guild-id, channel-id"),
    PREFIX("prefix", "prefix"),

    DBURL("dburl", ""),

    PORT("port", "4444");

    private final String key;
    private final String defaultValue;

    ConfigOption(String key, String defaultValue)
    {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public String getKey()
    {
        return key;
    }
}