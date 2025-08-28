package com.nullbyte.overseer;

import com.nullbyte.overseer.commands.Commands;
import com.nullbyte.overseer.listeners.Listeners;
import com.nullbyte.overseer.util.database.DatabaseManager;
import com.nullbyte.overseer.util.config.Config;
import com.nullbyte.overseer.util.config.DataConfig;
import com.nullbyte.overseer.util.config.MessagesConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;

public final class Overseer extends JavaPlugin {
    private static Overseer instance;
    @Getter private static DatabaseManager databaseManager = new DatabaseManager();
    public static Overseer get() {
        return instance;
    }
    public static void info(String msg) {
        instance.getLogger().info(msg);
    }

    public static BukkitTask runAsync(Runnable task) {
        return instance.getServer().getScheduler().runTaskAsynchronously(instance, task);
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        loadModules();
        getDatabaseManager().setup();
        info("Overseer has started successfully.");
    }

    @Override
    public void onDisable() {
        try {
            DataConfig.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getDatabaseManager().shutdown();
        info("Overseer has been disabled.");
    }
    private void loadModules() {
        Commands.register();
        Listeners.register();
    }
    private void loadConfig() {
        DataConfig.load();
        MessagesConfig.load();
        Config.load();
    }
}
