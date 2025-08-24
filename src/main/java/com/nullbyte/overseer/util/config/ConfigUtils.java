package com.nullbyte.overseer.util.config;

import com.nullbyte.overseer.Overseer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigUtils {
    public static YamlConfiguration copy(String name) {
        File configFile = new File(Overseer.get().getDataFolder(), name);
        if (!configFile.exists()) {
            Overseer.get().saveResource(name, false);
        }

        YamlConfiguration diskConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = Overseer.get().getResource(name);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            diskConfig.setDefaults(defaultConfig);
            diskConfig.options().parseComments(true);
            diskConfig.options().copyDefaults(true);
        }
        try {
            diskConfig.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return diskConfig;
    }
}
