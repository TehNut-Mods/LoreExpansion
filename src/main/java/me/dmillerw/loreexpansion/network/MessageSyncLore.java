package me.dmillerw.loreexpansion.network;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import me.dmillerw.loreexpansion.client.gui.GuiJournal;
import me.dmillerw.loreexpansion.client.sound.LESoundHandler;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.player.PlayerEventHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MessageSyncLore implements IMessage {

    private List<LoreKey> playerLore;

    public MessageSyncLore(EntityPlayerMP player) {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        this.playerLore = Lists.newArrayList(PlayerEventHandler.getData(world).getDataForPlayer(player));
    }

    public MessageSyncLore() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerLore = Lists.newArrayList();
        int loreSize = buf.readInt();
        for (int i = 0; i < loreSize; i++) {
            String id = ByteBufUtils.readUTF8String(buf);
            String category = ByteBufUtils.readUTF8String(buf);
            playerLore.add(new LoreKey(id, category));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerLore.size());
        for (LoreKey lore : playerLore) {
            ByteBufUtils.writeUTF8String(buf, lore.getId());
            ByteBufUtils.writeUTF8String(buf, lore.getCategory());
        }
    }

    public List<LoreKey> getPlayerLore() {
        return playerLore;
    }

    public static class Handler implements IMessageHandler<MessageSyncLore, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(MessageSyncLore message, MessageContext ctx) {
            GuiJournal.playerLore.clear();
            GuiJournal.playerLore = message.getPlayerLore();

            if (!GuiJournal.playerLore.contains(GuiJournal.selectedLore)) {
                GuiJournal.selectedLore = null;
                LESoundHandler.INSTANCE.stop();
            }
            return null;
        }
    }
}
