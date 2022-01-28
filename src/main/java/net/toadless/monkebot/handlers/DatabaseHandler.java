package net.toadless.monkebot.handlers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.bot.ConfigOption;
import net.toadless.monkebot.objects.bot.Configuration;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
    private final Monke monke;
    private final MongoClient connection;
    private String db;

    public DatabaseHandler(Monke monke)
    {
        LOGGER.debug("Starting local database pool.");
        this.monke = monke;
        this.connection = initClient();
    }

    public MongoClient getConnection()
    {
        return connection;
    }

    private MongoClient initClient()
    {
        LOGGER.debug("Starting local db setup.");
        MongoClientSettings.Builder clientConfig = MongoClientSettings.builder();
        Configuration configuration = monke.getConfiguration();

        ConnectionString connectionString = new ConnectionString(configuration.getString(ConfigOption.DBURL));
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        clientConfig.applyConnectionString(connectionString);
        clientConfig.codecRegistry(pojoCodecRegistry);

        this.db = connectionString.getDatabase();
        LOGGER.debug("Local db setup complete.");
        try
        {
            return MongoClients.create(clientConfig.build());
        }
        catch (Exception exception)
        {
            monke.getLogger().error("Local database offline, connection failure.");
            System.exit(1);
            return null;
        }
    }

    public MongoDatabase getDatabase()
    {
        return connection.getDatabase(db);
    }

    public void close()
    {
        LOGGER.debug("Closed database connection.");
        connection.close();
    }
}