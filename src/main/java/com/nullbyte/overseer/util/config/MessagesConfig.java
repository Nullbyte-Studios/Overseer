package com.nullbyte.overseer.util.config;

import com.nullbyte.overseer.Overseer;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@UtilityClass
public class MessagesConfig {
    private static final String name = "messages.yml";

    @Getter private static String featureNotAvailable;
    @Getter private static String staffChatEnabled;
    @Getter private static String staffChatDisabled;
    @Getter private static String adminChatEnabled;
    @Getter private static String adminChatDisabled;
    @Getter private static String staffChatMuted;
    @Getter private static String staffChatUnmuted;
    @Getter private static String adminChatMuted;
    @Getter private static String adminChatUnmuted;

    private void load() {
        featureNotAvailable = document.getString("feature-not-available");
        staffChatEnabled = document.getString("staff-chat.enabled");
        staffChatDisabled = document.getString("staff-chat.disabled");
        adminChatEnabled = document.getString("admin-chat.enabled");
        adminChatDisabled = document.getString("admin-chat.disabled");
        staffChatMuted = document.getString("staff-chat.muted");
        staffChatUnmuted = document.getString("staff-chat.unmuted");
        adminChatMuted = document.getString("admin-chat.muted");
        adminChatUnmuted = document.getString("admin-chat.unmuted");
    }

    private static final File configFile;
    private static YamlConfiguration document;
    static {
        configFile = new File(Overseer.get().getDataFolder(), name);
        if (!configFile.exists()) {
            if(!configFile.getParentFile().mkdirs()) {
                Overseer.get().getLogger().severe("Could not create data folder! Disabling...");
                Bukkit.getPluginManager().disablePlugin(Overseer.get());
            }
            Overseer.get().saveResource(name, false);
        }
        document = YamlConfiguration.loadConfiguration(configFile);
        load();
    }

    public void reload() {
        document = YamlConfiguration.loadConfiguration(configFile);
        load();
    }
}
