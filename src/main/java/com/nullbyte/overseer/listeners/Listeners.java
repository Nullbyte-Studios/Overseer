package com.nullbyte.overseer.listeners;

import com.nullbyte.overseer.Overseer;
import org.bukkit.Bukkit;

import java.util.List;

public class Listeners {
    public static void register() {
        List.of(
                new ChatEvent()
        ).forEach(e -> Bukkit.getServer().getPluginManager().registerEvents(e, Overseer.get()));
    }
}
