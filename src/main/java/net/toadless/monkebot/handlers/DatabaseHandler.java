package net.toadless.monkebot.handlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.bot.ConfigOption;
import net.toadless.monkebot.objects.bot.Configuration;
import net.toadless.monkebot.util.IOUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
    private final Monke monke;
    private final HikariDataSource pool;

    public DatabaseHandler(Monke monke)
    {
        LOGGER.debug("Starting local database pool.");
        this.monke = monke;
        this.pool = initHikari();
        initTables();
        System.getProperties().setProperty("org.jooq.no-logo", "true");
    }

    private void initTables()
    {
        LOGGER.debug("Initialise table guilds.");
        initTable("guilds");

        LOGGER.debug("Initialise table roles.");
        initTable("roles");

        LOGGER.debug("Initialise table tempbans.");
        initTable("tempbans");

        LOGGER.debug("Initialise table reaction_roles.");
        initTable("reaction_roles");

        LOGGER.debug("Initialise table reports.");
        initTable("reports");

        LOGGER.debug("Initialise table warnings.");
        initTable("warnings");

        LOGGER.debug("Initialise table word_blacklists.");
        initTable("word_blacklists");

        LOGGER.debug("Initialise table channel_blacklists.");
        initTable("channel_blacklists");

        LOGGER.debug("Table setup complete.");
    }

    public HikariDataSource getPool()
    {
        return pool;
    }

    public Connection getConnection()
    {
        try
        {
            return pool.getConnection();
        }
        catch (Exception exception)
        {
            return getConnection();
        }
    }

    private void initTable(String table)
    {
        try
        {
            InputStream file = IOUtils.getResourceFile("sql/" + table + ".sql");
            if (file == null)
            {
                throw new NullPointerException("File for table '" + table + "' not found");
            }
            getConnection().createStatement().execute(IOUtils.convertToString(file));
        }
        catch (Exception exception)
        {
            monke.getLogger().error("Error initializing table: '" + table + "'", exception);
        }
    }

    private HikariDataSource initHikari()
    {
        LOGGER.debug("Starting local HikariCP setup.");
        HikariConfig hikariConfig = new HikariConfig();
        Configuration configuration = monke.getConfiguration();

        hikariConfig.setDriverClassName(configuration.getString(ConfigOption.DBDRIVER));
        hikariConfig.setJdbcUrl(configuration.getString(ConfigOption.DBURL));

        hikariConfig.setUsername(monke.getConfiguration().getString(ConfigOption.DBUSERNAME));
        hikariConfig.setPassword(monke.getConfiguration().getString(ConfigOption.DBPASSWORD));

        hikariConfig.setMaximumPoolSize(30);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setConnectionTimeout(10000);
        LOGGER.debug("Local HikariCP setup complete.");
        try
        {
            return new HikariDataSource(hikariConfig);
        }
        catch (Exception exception)
        {
            monke.getLogger().error("Local database offline, connection failure.");
            System.exit(1);
            return null;
        }
    }

    public DSLContext getContext()
    {
        return getContext(getConnection());
    }

    public DSLContext getContext(Connection connection)
    {
        return DSL.using(connection, SQLDialect.POSTGRES);
    }

    public void close()
    {
        LOGGER.debug("Closed local database.");
        pool.close();
    }
}