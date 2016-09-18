package me.dmillerw.loreexpansion.network;

import io.netty.buffer.ByteBuf;
import me.dmillerw.loreexpansion.util.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOverlayLore implements IMessage {

    private String textBody;
    private String loreName;

    public MessageOverlayLore(String textBody, String loreName) {
        this.textBody = textBody;
        this.loreName = loreName;
    }

    public MessageOverlayLore() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        textBody = ByteBufUtils.readUTF8String(buf);
        loreName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, textBody);
        ByteBufUtils.writeUTF8String(buf, loreName);
    }

    public String getTextBody() {
        return textBody;
    }

    public String getLoreName() {
        return loreName;
    }

    public static class Handler implements IMessageHandler<MessageOverlayLore, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(MessageOverlayLore message, MessageContext ctx) {
            String toDraw = StringHelper.getLocalizedText(message.getTextBody(), StringHelper.getLocalizedText(message.getLoreName()));
            Minecraft.getMinecraft().ingameGUI.setRecordPlaying(toDraw, false);
            return null;
        }
    }
}
