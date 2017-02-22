package me.dmillerw.loreexpansion.command;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandReloadLore extends CommandBase {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/lore reload";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        LoreLoader.init(LoreExpansion.loreDir, false);
        for (EntityPlayer player : server.getPlayerList().getPlayers())
            LoreUtil.checkDefaults(player);
        getCommandSenderAsPlayer(sender).sendMessage(new TextComponentTranslation("chat.loreexpansion.lore.reloaded"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
}
