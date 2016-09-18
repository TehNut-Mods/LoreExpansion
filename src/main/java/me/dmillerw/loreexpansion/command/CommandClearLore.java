package me.dmillerw.loreexpansion.command;

import me.dmillerw.loreexpansion.core.saving.LoreSaveData;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
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

        EntityPlayer player = getCommandSenderAsPlayer(sender);
        if (args.length > 0)
            player = getPlayer(server, sender, args[0]);

        LoreSaveData loreSaveData = LoreUtil.getData(world);
        loreSaveData.clearPlayer(player);
    }
}
