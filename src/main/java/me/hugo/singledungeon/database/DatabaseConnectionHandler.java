package me.hugo.singledungeon.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hugo.singledungeon.SingleDungeon;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionHandler {

    private final HikariDataSource dataSource;

    public DatabaseConnectionHandler(SingleDungeon main) {
        FileConfiguration config = main.getConfig();

        String ip, port, schema;

        ip = config.getString("database.ip", "localhost");
        port = config.getString("database.port", "3306");
        schema = config.getString("database.schema", "single_dungeon_data");

        Properties sqlProperties = new Properties();

        sqlProperties.setProperty("jdbcUrl", "jdbc:mysql://" + ip + ":" + port + "/" + schema);
        sqlProperties.setProperty("dataSource.serverName", ip);
        sqlProperties.setProperty("dataSource.portNumber", port);
        sqlProperties.setProperty("dataSource.user", config.getString("database.user", "root"));
        sqlProperties.setProperty("dataSource.password", config.getString("database.password", ""));
        sqlProperties.setProperty("dataSource.databaseName", schema);

        HikariConfig configuration = new HikariConfig(sqlProperties);
        configuration.setMaximumPoolSize(10);

        this.dataSource = new HikariDataSource(configuration);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


}
