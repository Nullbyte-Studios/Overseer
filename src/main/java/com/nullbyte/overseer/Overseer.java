package com.nullbyte.overseer;

import com.nullbyte.overseer.commands.Commands;
import com.nullbyte.overseer.listeners.Listeners;
import com.nullbyte.overseer.util.config.DataConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Overseer extends JavaPlugin {
    private static Overseer instance;
    public static Overseer get() {
        return instance;
    }
    public static void info(String msg) {
        instance.getLogger().info(msg);
    }

    @Override
    public void onEnable() {
        instance = this;
        DataConfig.load();
        Commands.register();
        Listeners.register();
        info("Overseer has started successfully.");
    }

    @Override
    public void onDisable() {
        try {
            DataConfig.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        info("Overseer has been disabled.");
    }
}
