package com.nullbyte.overseer.util.config;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.YamlConfiguration;

@UtilityClass
public class MessagesConfig {
    private static final String name = "messages.yml";

    @Getter private static String featureNotAvailable;

    @Getter private static String staffChatEnabled;
    @Getter private static String staffChatDisabled;
    @Getter private static String staffChatMuted;
    @Getter private static String staffChatUnmuted;
    @Getter private static String staffChatReminder;
    @Getter private static String staffChatMutedReminder;

    @Getter private static String adminChatEnabled;
    @Getter private static String adminChatDisabled;
    @Getter private static String adminChatMuted;
    @Getter private static String adminChatUnmuted;
    @Getter private static String adminChatReminder;
    @Getter private static String adminChatMutedReminder;

    public static void load() {
        YamlConfiguration document = ConfigUtils.copy(name);

        featureNotAvailable = document.getString("feature-not-available");

        staffChatEnabled = document.getString("staff-chat.enabled");
        staffChatDisabled = document.getString("staff-chat.disabled");
        staffChatMuted = document.getString("staff-chat.muted");
        staffChatUnmuted = document.getString("staff-chat.unmuted");
        staffChatReminder = document.getString("staff-chat.reminder");
        staffChatMutedReminder = document.getString("staff-chat.muted-reminder");

        adminChatEnabled = document.getString("admin-chat.enabled");
        adminChatDisabled = document.getString("admin-chat.disabled");
        adminChatMuted = document.getString("admin-chat.muted");
        adminChatUnmuted = document.getString("admin-chat.unmuted");
        adminChatReminder = document.getString("admin-chat.reminder");
        adminChatMutedReminder = document.getString("admin-chat.muted-reminder");
    }
}
