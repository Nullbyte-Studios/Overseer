package com.nullbyte.overseer.util;

import com.nullbyte.overseer.util.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageUtil {
    public static MiniMessage mm = MiniMessage.miniMessage();
    public static Component transform(String text) {
        return mm.deserialize(LegacyConverter.legacyToAdventure(text));
    }

    public static Component transformPrefix(String text) {
        return transform(Config.getPrefix()).append(transform(text));
    }
    public static Component transformPrefix(String text, String prefix) {
        return transform(prefix).append(transform(text));
    }
}
