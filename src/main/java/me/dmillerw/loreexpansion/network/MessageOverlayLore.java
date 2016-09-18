package me.dmillerw.loreexpansion.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOverlayLore implements IMessage {

    private String toDraw;

    public MessageOverlayLore(String toDraw) {
        this.toDraw = toDraw;
    }

    public MessageOverlayLore() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        toDraw = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, toDraw);
    }

    public String getToDraw() {
        return toDraw;
    }

    public static class Handler implements IMessageHandler<MessageOverlayLore, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(MessageOverlayLore message, MessageContext ctx) {
            Minecraft.getMinecraft().ingameGUI.setRecordPlaying(message.getToDraw(), false);
            return null;
        }
    }
}
