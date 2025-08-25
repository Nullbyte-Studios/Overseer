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
    /**
     * Initializes the application's database connection and triggers schema creation.
     *
     * Chooses the database backend based on Config.getDatabaseType() (supports "mysql" and "sqlite"),
     * performs the corresponding setup, logs a successful connection, and asynchronously runs
     * createTables() to ensure required tables exist.
     */
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

    /**
     * Ensures the required database schema exists by creating the "players" and "notes" tables if they do not.
     *
     * <p>Behavior depends on the configured database type:
     * <ul>
     *   <li>MySQL: creates `players(uuid VARCHAR(36) PRIMARY KEY)` and `notes` with `id INT AUTO_INCREMENT`, `player_uuid VARCHAR(36)`, `moderator_uuid VARCHAR(36)`, `message TEXT`, `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`, and a foreign key on `player_uuid` that cascades on delete.</li>
     *   <li>SQLite: creates `players(uuid TEXT PRIMARY KEY)` and `notes` with `id INTEGER PRIMARY KEY AUTOINCREMENT`, `player_uuid TEXT`, `moderator_uuid TEXT`, `message TEXT`, `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`, and a foreign key on `player_uuid` that cascades on delete. SQLite foreign keys are enabled elsewhere in initialization.</li>
     * </ul>
     *
     * <p>The method executes DDL statements through the shared Jdbi instance.
     */
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
    /**
     * Configures and starts a HikariCP data source for MySQL and initializes the static Jdbi instance.
     *
     * Uses connection settings from Config (host, port, database, user, password, max pool size)
     * to build the JDBC URL and HikariConfig, creates the HikariDataSource, and assigns the resulting
     * Jdbi instance to the class static field.
     */
    private void setupMySQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + Config.getMysqlHost() + ":" + Config.getMysqlPort() + "/" + Config.getMysqlDatabase() + "?useSSL=false&serverTimezone=UTC");
        config.setUsername(Config.getMysqlUser());
        config.setPassword(Config.getMysqlPassword());
        config.setMaximumPoolSize(Config.getMysqlMaxPoolSize());
        hikari = new HikariDataSource(config);
        jdbi = Jdbi.create(hikari);
    }
    /**
     * Initializes the static Jdbi instance for an embedded SQLite database and enables foreign key enforcement.
     *
     * The Jdbi instance is created using a JDBC URL pointing to the SQLite file located in the application's
     * data folder (Overseer.get().getDataFolder() / Config.getSqliteFile()). After creation, this method
     * executes `PRAGMA foreign_keys = ON` to ensure SQLite enforces FOREIGN KEY constraints.
     */
    private void setupSQLite() {
        jdbi = Jdbi.create("jdbc:sqlite:" + new File(Overseer.get().getDataFolder(), Config.getSqliteFile()).getAbsolutePath());
        jdbi.useHandle(h -> h.execute("PRAGMA foreign_keys = ON"));
    }

    /**
     * Shuts down the database connection pool.
     *
     * Closes the HikariDataSource if it exists and is not already closed, and logs a disconnection message.
     */
    public void shutdown() {
        if (hikari != null && !hikari.isClosed()) hikari.close();
        Overseer.info("Disconnected from database");
    }
}
