package com.nullbyte.overseer.listeners;

import com.nullbyte.overseer.util.MessageUtil;
import com.nullbyte.overseer.util.config.Config;
import com.nullbyte.overseer.util.config.DataConfig;
import com.nullbyte.overseer.util.config.MessagesConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("overseer.staff") && Config.isStaffChatEnabled()) {
            if (DataConfig.isStaffChatEnabled(player.getUniqueId()))
                player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getStaffChatReminder(), Config.getStaffChatPrefix()));
            if (DataConfig.isStaffMuted(player.getUniqueId()) && Config.isStaffChatMuteEnabled())
                player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getStaffChatMutedReminder(), Config.getStaffChatPrefix()));
        }
        if (player.hasPermission("overseer.admin") && Config.isAdminChatEnabled()) {
            if (DataConfig.isAdminChatEnabled(player.getUniqueId()))
                player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getAdminChatReminder(), Config.getAdminChatPrefix()));
            if (DataConfig.isAdminMuted(player.getUniqueId()) && Config.isAdminChatMuteEnabled())
                player.sendMessage(MessageUtil.transformPrefix(MessagesConfig.getAdminChatMutedReminder(), Config.getAdminChatPrefix()));
        }
    }
}
