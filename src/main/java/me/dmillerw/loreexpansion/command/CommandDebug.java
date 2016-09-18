package me.dmillerw.loreexpansion.command;

import com.google.common.base.Objects;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

public class CommandDebug extends CommandBase {

    @Override
    public String getCommandName() {
        return "debug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/lore debug";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        LoreExpansion.LOGGER.info(Objects.toStringHelper(Map.class).add("playerData", LoreUtil.getData(server.getEntityWorld()).getPlayerData()));
    }
}
