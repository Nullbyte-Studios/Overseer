package com.nullbyte.overseer.listeners;

import com.nullbyte.overseer.commands.StaffChatCommands;
import com.nullbyte.overseer.util.config.Config;
import com.nullbyte.overseer.util.config.DataConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.regex.Pattern;

public class ChatEvent implements Listener {
    @EventHandler
    public void onChatEvent(AsyncChatEvent e) {
        Player player = e.getPlayer();
        Component original = e.message();

        if (player.hasPermission("overseer.staff") && Config.isStaffChatEnabled()) {
            Pattern staffPattern = Pattern.compile("^" + Pattern.quote(Config.getStaffChatHandle()));
            Component stripped = original.replaceText(b -> b.match(staffPattern).replacement(""));

            if (DataConfig.isStaffChatEnabled(player.getUniqueId()) ||
                    (Config.isStaffChatHandleEnabled() && !stripped.equals(original))) {
                StaffChatCommands.broadcastStaffMessage(player, stripped);
                e.setCancelled(true);
            }
        }

        if (player.hasPermission("overseer.admin") && Config.isAdminChatEnabled()) {
            Pattern adminPattern = Pattern.compile("^" + Pattern.quote(Config.getAdminChatHandle()));
            Component stripped = original.replaceText(b -> b.match(adminPattern).replacement(""));

            if (DataConfig.isAdminChatEnabled(player.getUniqueId()) ||
                    (Config.isAdminChatHandleEnabled() && !stripped.equals(original))) {
                StaffChatCommands.broadcastAdminMessage(player, stripped);
                e.setCancelled(true);
            }
        }
    }
}
