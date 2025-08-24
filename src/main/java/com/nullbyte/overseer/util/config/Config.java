package com.nullbyte.overseer.util.config;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.YamlConfiguration;

@UtilityClass
public class Config {
    private static final String name = "config.yml";

    @Getter private String prefix;
    @Getter private boolean staffChatEnabled;
    @Getter private String staffChatPrefix;
    @Getter private String staffChatMessageFormat;
    @Getter private boolean staffChatMuteEnabled;
    @Getter private boolean staffChatSoundEnabled;
    @Getter private String staffChatSound;
    @Getter private boolean staffChatHandleEnabled;
    @Getter private String staffChatHandle;
    @Getter private boolean adminChatEnabled;
    @Getter private String adminChatPrefix;
    @Getter private String adminChatMessageFormat;
    @Getter private boolean adminChatMuteEnabled;
    @Getter private boolean adminChatSoundEnabled;
    @Getter private String adminChatSound;
    @Getter private boolean adminChatHandleEnabled;
    @Getter private String adminChatHandle;

    public static void load() {
        YamlConfiguration document = ConfigUtils.copy(name);

        prefix = document.getString("prefix");

        staffChatEnabled = document.getBoolean("features.staff-chat.enabled");
        staffChatPrefix = document.getString("features.staff-chat.prefix");
        staffChatMessageFormat = document.getString("features.staff-chat.message-format");
        staffChatMuteEnabled = document.getBoolean("features.staff-chat.mute");
        staffChatSoundEnabled = document.getBoolean("features.staff-chat.sound.enabled");
        staffChatSound = document.getString("features.staff-chat.sound.sound-name");
        staffChatHandleEnabled = document.getBoolean("features.staff-chat.handle.enabled");
        staffChatHandle = document.getString("features.staff-chat.handle.text");

        adminChatEnabled = document.getBoolean("features.admin-chat.enabled");
        adminChatPrefix = document.getString("features.admin-chat.prefix");
        adminChatMessageFormat = document.getString("features.admin-chat.message-format");
        adminChatMuteEnabled = document.getBoolean("features.admin-chat.mute");
        adminChatSoundEnabled = document.getBoolean("features.admin-chat.sound.enabled");
        adminChatSound = document.getString("features.admin-chat.sound.sound-name");
        adminChatHandleEnabled = document.getBoolean("features.admin-chat.handle.enabled");
        adminChatHandle = document.getString("features.admin-chat.handle.text");
    }
}
