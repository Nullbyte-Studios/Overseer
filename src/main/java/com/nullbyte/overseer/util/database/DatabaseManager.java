package com.nullbyte.overseer.util.database;

import com.nullbyte.overseer.Overseer;
import com.nullbyte.overseer.util.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    @Getter 
    private static HikariDataSource dataSource;
    
    public void setup() {
        if ("mysql".equals(Config.getDatabaseType())) {
            setupMySQL();
        } else if ("sqlite".equals(Config.getDatabaseType())) {
            setupSQLite();
        }
        Overseer.info("Connected to database.");
        Overseer.runAsync(this::createTables);
    }

    private void createTables() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            if (Config.getDatabaseType().equalsIgnoreCase("mysql")) {
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS players (" +
                                "uuid VARCHAR(36) PRIMARY KEY)"
                );
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS notes (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "player_uuid VARCHAR(36) NOT NULL, " +
                                "moderator_uuid VARCHAR(36) NOT NULL, " +
                                "message TEXT NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY(player_uuid) REFERENCES players(uuid) ON DELETE CASCADE)"
                );
            } else {
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS players (" +
                                "uuid TEXT PRIMARY KEY)"
                );
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS notes (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "player_uuid TEXT NOT NULL, " +
                                "moderator_uuid TEXT NOT NULL, " +
                                "message TEXT NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY(player_uuid) REFERENCES players(uuid) ON DELETE CASCADE)"
                );
            }
        } catch (SQLException e) {
            Overseer.get().getLogger().severe("Failed to create tables: " + e.getMessage());
        }
    }

    private static void setupMySQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + Config.getMysqlHost() + ":" + Config.getMysqlPort() + "/" + Config.getMysqlDatabase() + "?useSSL=false&serverTimezone=UTC");
        config.setUsername(Config.getMysqlUser());
        config.setPassword(Config.getMysqlPassword());
        config.setMaximumPoolSize(Config.getMysqlMaxPoolSize());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource = new HikariDataSource(config);
    }
    
    private static void setupSQLite() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + new File(Overseer.get().getDataFolder(), Config.getSqliteFile()).getAbsolutePath());
        config.setDriverClassName("org.sqlite.JDBC");
        dataSource = new HikariDataSource(config);
        
        // Enable foreign keys for SQLite
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            Overseer.get().getLogger().severe("Failed to enable foreign keys: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            Overseer.info("Disconnected from database");
        }
    }
}
