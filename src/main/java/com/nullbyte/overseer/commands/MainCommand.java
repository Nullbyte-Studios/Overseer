package com.nullbyte.overseer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.nullbyte.overseer.Overseer;
import com.nullbyte.overseer.util.config.Config;
import com.nullbyte.overseer.util.MessageUtil;
import com.nullbyte.overseer.util.config.MessagesConfig;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

@CommandAlias("overseer|ov")
@Description("The main command of Overseer")
public class MainCommand extends BaseCommand {
    @Default
    public void onDefault(CommandSender sender) {
        PluginMeta meta = Overseer.get().getPluginMeta();
        sender.sendMessage(MessageUtil.transformPrefix("&2This server is using " + meta.getDisplayName()));
    }
    @Subcommand("reload|rl")
    @CommandAlias("ovreload")
    @Description("Reload the plugin.")
    @CommandPermission("overseer.reload")
    public void onReload(CommandSender sender) throws IOException, InvalidConfigurationException {
        Config.reload();
        MessagesConfig.reload();
        sender.sendMessage(MessageUtil.transformPrefix("&aReloaded configuration."));
    }
}
