package com.nullbyte.overseer.util;

import com.nullbyte.overseer.Overseer;
import com.nullbyte.overseer.util.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.io.File;

public class DatabaseManager {
    @Getter private static Jdbi jdbi;
    public void setup() {
        switch (Config.getDatabaseType()) {
            case "mysql" -> {
                setupMySQL();
            }
            case "sqlite" -> {
                setupSQLite();
            }
        }
        Overseer.info("Connected to database.");
        Overseer.runAsync((task) -> this.createTables());
    }

    private void createTables() {
        jdbi.useHandle(handle -> {
            if (Config.getDatabaseType().equalsIgnoreCase("mysql")) {
                handle.execute(
                        "CREATE TABLE IF NOT EXISTS players (" +
                                "uuid VARCHAR(36) PRIMARY KEY)"
                );
                handle.execute(
                        "CREATE TABLE IF NOT EXISTS notes (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "player_uuid VARCHAR(36) NOT NULL, " +
                                "moderator_uuid VARCHAR(36) NOT NULL, " +
                                "message TEXT NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY(player_uuid) REFERENCES players(uuid) ON DELETE CASCADE)"
                );
            } else {
                handle.execute(
                        "CREATE TABLE IF NOT EXISTS players (" +
                                "uuid TEXT PRIMARY KEY)"
                );
                handle.execute(
                        "CREATE TABLE IF NOT EXISTS notes (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "player_uuid TEXT NOT NULL, " +
                                "moderator_uuid TEXT NOT NULL, " +
                                "message TEXT NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY(player_uuid) REFERENCES players(uuid) ON DELETE CASCADE)"
                );
            }
        });
    }
    private static HikariDataSource hikari;
    private void setupMySQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + Config.getMysqlHost() + ":" + Config.getMysqlPort() + "/" + Config.getMysqlDatabase() + "?useSSL=false&serverTimezone=UTC");
        config.setUsername(Config.getMysqlUser());
        config.setPassword(Config.getMysqlPassword());
        config.setMaximumPoolSize(Config.getMysqlMaxPoolSize());
        hikari = new HikariDataSource(config);
        jdbi = Jdbi.create(hikari);
    }
    private void setupSQLite() {
        jdbi = Jdbi.create("jdbc:sqlite:" + new File(Overseer.get().getDataFolder(), Config.getSqliteFile()).getAbsolutePath());
        jdbi.useHandle(h -> h.execute("PRAGMA foreign_keys = ON"));
    }

    public void shutdown() {
        if (hikari != null && !hikari.isClosed()) hikari.close();
        Overseer.info("Disconnected from database");
    }
}
