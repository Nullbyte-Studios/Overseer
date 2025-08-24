package com.nullbyte.overseer.commands;

import co.aikar.commands.PaperCommandManager;
import com.nullbyte.overseer.Overseer;
import lombok.Getter;

public class Commands {
    @Getter private static PaperCommandManager manager = new PaperCommandManager(Overseer.get());
    public static void register() {
        manager.registerCommand(new MainCommand());
        manager.registerCommand(new StaffChatCommands());
    }
}
