package me.dmillerw.loreexpansion.command;

import com.google.common.base.Joiner;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandDebug extends CommandBase {

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/lore debug";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        LoreExpansion.LOGGER.info(Joiner.on("|").withKeyValueSeparator("_").useForNull("null").join(LoreUtil.getData(server.getEntityWorld()).getPlayerData()));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
}
