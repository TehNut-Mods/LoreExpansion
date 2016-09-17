package me.dmillerw.loreexpansion.command;

import me.dmillerw.loreexpansion.core.player.LoreSaveData;
import me.dmillerw.loreexpansion.core.player.PlayerEventHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommandClearLore extends CommandBase {

    @Override
    public String getCommandName() {
        return "clear";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/lore clear";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = server.getEntityWorld();
        if (world.isRemote)
            return;

        LoreSaveData loreSaveData = PlayerEventHandler.getData(world);
        loreSaveData.clearPlayer(getCommandSenderAsPlayer(sender));
    }
}
