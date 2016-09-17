package me.dmillerw.loreexpansion.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandReloadLore extends CommandBase {

    @Override
    public String getCommandName() {
        return "reload";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/lore reload";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

    }
}
