package me.dmillerw.loreexpansion.util;

import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CommandSenderLore implements ICommandSender {

    private final EntityPlayer player;

    public CommandSenderLore(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return LoreExpansion.ID;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public void sendMessage(ITextComponent component) {
        // No-op
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName) {
        return true;
    }

    @Override
    public BlockPos getPosition() {
        return player.getPosition();
    }

    @Override
    public Vec3d getPositionVector() {
        return player.getPositionVector();
    }

    @Override
    public World getEntityWorld() {
        return player.getEntityWorld();
    }

    @Nullable
    @Override
    public Entity getCommandSenderEntity() {
        return player;
    }

    @Override
    public boolean sendCommandFeedback() {
        return false;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {
        // No-op
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return player.getServer();
    }
}
