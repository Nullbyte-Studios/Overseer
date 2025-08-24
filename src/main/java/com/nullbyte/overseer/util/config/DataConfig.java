package com.nullbyte.overseer.util.config;

import com.nullbyte.overseer.Overseer;
import com.nullbyte.overseer.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DataConfig {
    private static final String name = "data.yml";

    private static final YamlConfiguration document;
    private static final Set<UUID> staffMuted = new HashSet<>();
    private static final Set<UUID> adminMuted = new HashSet<>();
    private static final Set<UUID> staffChatEnabled = new HashSet<>();
    private static final Set<UUID> adminChatEnabled = new HashSet<>();

    private static final File configFile;
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
    }
    public static void load() {
        for (String s : document.getStringList("staffMuted")) {
            staffMuted.add(UUID.fromString(s));
        }
        for (String s : document.getStringList("adminMuted")) {
            adminMuted.add(UUID.fromString(s));
        }
        for (String s : document.getStringList("staffChatEnabled")) {
            staffChatEnabled.add(UUID.fromString(s));
        }
        for (String s : document.getStringList("adminChatEnabled")) {
            adminChatEnabled.add(UUID.fromString(s));
        }
    }
    public static void save() throws IOException {
        document.set("staffMuted", staffMuted.stream().map(UUID::toString).toList());
        document.set("adminMuted", adminMuted.stream().map(UUID::toString).toList());
        document.set("staffChatEnabled", staffChatEnabled.stream().map(UUID::toString).toList());
        document.set("adminChatEnabled", adminChatEnabled.stream().map(UUID::toString).toList());
        document.save(configFile);
    }
    public static boolean toggleStaffMute(UUID uuid) {
        return Util.toggle(uuid, staffMuted);
    }
    public static boolean toggleAdminMute(UUID uuid) {
        return Util.toggle(uuid, adminMuted);
    }
    public static boolean toggleStaffChat(UUID uuid) {
        return Util.toggle(uuid, staffChatEnabled);
    }
    public static boolean toggleAdminChat(UUID uuid) {
        return Util.toggle(uuid, adminChatEnabled);
    }

    public static boolean isStaffMuted(UUID uuid) {
        return Config.isAdminChatMuteEnabled() && staffMuted.contains(uuid);
    }

    public static boolean isAdminMuted(UUID uuid) {
        return Config.isAdminChatMuteEnabled() && adminMuted.contains(uuid);
    }
    public static boolean isStaffChatEnabled(UUID uuid) {
        return Config.isStaffChatEnabled() && staffChatEnabled.contains(uuid);
    }
    public static boolean isAdminChatEnabled(UUID uuid) {
        return Config.isAdminChatEnabled() && adminChatEnabled.contains(uuid);
    }
}
