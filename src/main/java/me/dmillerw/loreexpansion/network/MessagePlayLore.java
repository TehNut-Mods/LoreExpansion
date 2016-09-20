package me.dmillerw.loreexpansion.network;

import io.netty.buffer.ByteBuf;
import me.dmillerw.loreexpansion.client.sound.LESoundHandler;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePlayLore implements IMessage {

    private LoreKey loreKey;

    public MessagePlayLore() {

    }

    public MessagePlayLore(LoreKey loreKey) {
        this.loreKey = loreKey;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String id = ByteBufUtils.readUTF8String(buf);
        String category = ByteBufUtils.readUTF8String(buf);
        this.loreKey = new LoreKey(id, category);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, loreKey.getId());
        ByteBufUtils.writeUTF8String(buf, loreKey.getCategory());
    }

    public LoreKey getLoreKey() {
        return loreKey;
    }

    public static class Handler implements IMessageHandler<MessagePlayLore, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(MessagePlayLore message, MessageContext ctx) {
            Lore lore = LoreLoader.getLore(message.getLoreKey());
            LESoundHandler.INSTANCE.play(lore.getContent().getAudio());
            return null;
        }
    }
}
