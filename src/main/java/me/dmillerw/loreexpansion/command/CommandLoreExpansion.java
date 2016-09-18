package me.dmillerw.loreexpansion.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandLoreExpansion extends CommandTreeBase {

    public CommandLoreExpansion() {
        addSubcommand(new CommandClearLore());
        addSubcommand(new CommandReloadLore());
        addSubcommand(new CommandDebug());
        addSubcommand(new CommandGiveLore());
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "lore";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/lore [clear|reload]";
    }
}
