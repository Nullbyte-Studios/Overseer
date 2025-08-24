// https://github.com/EternalCodeTeam/ChatFormatter

package com.nullbyte.overseer.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.regex.Pattern;

public final class LegacyConverter {

    private static final Pattern COLOR_LEGACY_PATTERN = Pattern.compile("(?i)&([0-9A-FK-ORX#])");
    private static final Pattern HEX_LEGACY_PATTERN = Pattern.compile("(?i)&#([0-9A-F]{6})");
    private static final Pattern HEX_LEGACY_VANILLA_PATTERN = Pattern.compile("(?i)&x(&[0-9A-F]){6}");

    private static final Map<Character, String> codeTranslations = new ImmutableMap.Builder<Character, String>()
            .put('0', "<black>")
            .put('1', "<dark_blue>")
            .put('2', "<dark_green>")
            .put('3', "<dark_aqua>")
            .put('4', "<dark_red>")
            .put('5', "<dark_purple>")
            .put('6', "<gold>")
            .put('7', "<gray>")
            .put('8', "<dark_gray>")
            .put('9', "<blue>")
            .put('a', "<green>")
            .put('b', "<aqua>")
            .put('c', "<red>")
            .put('d', "<light_purple>")
            .put('e', "<yellow>")
            .put('f', "<white>")
            .put('k', "<obfuscated>")
            .put('l', "<bold>")
            .put('m', "<strikethrough>")
            .put('n', "<underlined>")
            .put('o', "<italic>")
            .put('r', "<reset>")
            .build();

    public static String legacyToAdventure(String input) {
        String result = HEX_LEGACY_VANILLA_PATTERN.matcher(input).replaceAll(matchResult -> {
            String hexColor = matchResult.group().replace("&x", "").replace("&", "");
            return "<#" + hexColor + ">";
        });

        result = HEX_LEGACY_PATTERN.matcher(result).replaceAll(matchResult -> {
            String hex = matchResult.group(1);
            return "<#" + hex + ">";
        });

        result = COLOR_LEGACY_PATTERN.matcher(result).replaceAll(matchResult -> {
            char color = matchResult.group(1).toLowerCase().charAt(0);
            String adventure = codeTranslations.get(color);
            if (adventure != null) {
                return adventure;
            }
            return "&" + color;
        });

        return result;
    }
}