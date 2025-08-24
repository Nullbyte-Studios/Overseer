package com.nullbyte.overseer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.nullbyte.overseer.util.MessageUtil;
import com.nullbyte.overseer.util.config.Config;
import com.nullbyte.overseer.util.config.DataConfig;
import com.nullbyte.overseer.util.config.MessagesConfig;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public class StaffChatCommands extends BaseCommand {
    @CommandAlias("staffchat|sc")
    @CommandPermission("overseer.staff")
    public void onStaffChat(Player player, @Default("") String message) {
        if (!Config.isStaffChatEnabled()) {
            player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getFeatureNotAvailable()));
            return;
        }
        String rawPrefix = Config.getStaffChatPrefix();
        if (message.isEmpty()) {
            boolean enabled = DataConfig.toggleStaffChat(player.getUniqueId());
            player.sendMessage(MessageUtil.transformPrefix(enabled ? MessagesConfig.getStaffChatEnabled() : MessagesConfig.getStaffChatDisabled(), rawPrefix));
            return;
        }
        broadcastStaffMessage(player, MessageUtil.transform(message));
    }
    @CommandAlias("adminchat|ac")
    @CommandPermission("overseer.admin")
    public void onAdminChat(Player player, @Default("") String message) {
        String rawPrefix = Config.getAdminChatPrefix();
        if (message.isEmpty()) {
            boolean enabled = DataConfig.toggleAdminChat(player.getUniqueId());
            player.sendMessage(MessageUtil.transformPrefix(enabled ? MessagesConfig.getAdminChatEnabled() : MessagesConfig.getAdminChatDisabled(), rawPrefix));
            return;
        }
        broadcastAdminMessage(player, MessageUtil.transform(message));
    }

    @CommandAlias("mutestaffchat|mutesc")
    @CommandPermission("overseer.staff")
    public void muteStaffChat(Player player) {
        if(!Config.isStaffChatMuteEnabled()) {
            player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getFeatureNotAvailable()));
            return;
        }
        boolean muted = DataConfig.toggleStaffMute(player.getUniqueId());
        player.sendMessage(MessageUtil.transformPrefix(muted ? MessagesConfig.getStaffChatMuted() : MessagesConfig.getStaffChatUnmuted()));
    }

    @CommandAlias("muteadminchat|muteac")
    @CommandPermission("overseer.admin")
    public void muteAdminChat(Player player) {
        if(!Config.isAdminChatMuteEnabled()) {
            player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getFeatureNotAvailable()));
            return;
        }
        boolean muted = DataConfig.toggleAdminMute(player.getUniqueId());
        player.sendMessage(MessageUtil.transformPrefix(muted ? MessagesConfig.getAdminChatMuted() : MessagesConfig.getAdminChatUnmuted()));
    }
    public static void broadcastStaffMessage(Player player, Component message) {
        String rawPrefix = Config.getStaffChatPrefix();
        if(!DataConfig.getStaffMuted().isEmpty() && !Config.isStaffChatMuteEnabled()) DataConfig.setStaffMuted(Set.of());
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (!staff.hasPermission("overseer.staff") || DataConfig.isStaffMuted(staff.getUniqueId())) continue;
            Component msg = MessageUtil.transformPrefix(Config.getStaffChatMessageFormat(), rawPrefix)
                    .replaceText(b -> b.matchLiteral("{player}").replacement(player.displayName()))
                    .replaceText(b -> b.matchLiteral("{message}").replacement(message));
            staff.sendMessage(msg);
            if (staff != player && Config.isStaffChatSoundEnabled()) {
                staff.playSound(Sound.sound(Key.key(Config.getStaffChatSound()), Sound.Source.MASTER, 1f, 1f));
            }
        }
    }
    public static void broadcastAdminMessage(Player player, Component message) {
        String rawPrefix = Config.getAdminChatPrefix();
        if(!DataConfig.getAdminMuted().isEmpty() && !Config.isAdminChatMuteEnabled()) DataConfig.setAdminMuted(Set.of());
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (!admin.hasPermission("overseer.admin") || DataConfig.isAdminMuted(admin.getUniqueId())) continue;
            Component msg = MessageUtil.transformPrefix(Config.getAdminChatMessageFormat(), rawPrefix)
                    .replaceText(b -> b.matchLiteral("{player}").replacement(player.displayName()))
                    .replaceText(b -> b.matchLiteral("{message}").replacement(message));
            admin.sendMessage(msg);
            if (admin != player && Config.isAdminChatSoundEnabled()) {
                admin.playSound(Sound.sound(Key.key(Config.getAdminChatSound()), Sound.Source.MASTER, 1f, 1f));
            }
        }
    }
}
