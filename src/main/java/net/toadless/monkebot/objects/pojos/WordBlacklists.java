package net.toadless.monkebot.objects.pojos;

import java.io.Serializable;

/**
 * This class is a reference of what we pull from the database.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class WordBlacklists implements Serializable
{
    private Long guildId;
    private String phrase;

    public WordBlacklists(WordBlacklists value)
    {
        this.guildId = value.guildId;
        this.phrase = value.phrase;
    }

    public WordBlacklists(
            Long guildId,
            String phrase
    )
    {
        this.guildId = guildId;
        this.phrase = phrase;
    }

    public WordBlacklists() {}

    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public void setPhrase(String phrase)
    {
        this.phrase = phrase;
    }

    public Long getGuildId()
    {
        return guildId;
    }

    public String getPhrase()
    {
        return phrase;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("WordBlacklists (");

        sb.append(guildId);
        sb.append(", ").append(phrase);

        sb.append(")");
        return sb.toString();
    }
}