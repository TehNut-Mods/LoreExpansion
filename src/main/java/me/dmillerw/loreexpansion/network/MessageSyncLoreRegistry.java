package me.dmillerw.loreexpansion.network;

import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.Serializers;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.Set;

public class MessageSyncLoreRegistry implements IMessage {

    public static Set<Lore> LORE_BACKUP;

    private String loreJson;

    public MessageSyncLoreRegistry() {

    }

    public MessageSyncLoreRegistry(String loreJson) {
        this.loreJson = loreJson;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        loreJson = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, loreJson);
    }

    public String getLoreJson() {
        return loreJson;
    }

    public static class Handler implements IMessageHandler<MessageSyncLoreRegistry, IMessage> {
        @Override
        public IMessage onMessage(MessageSyncLoreRegistry message, MessageContext ctx) {
            LORE_BACKUP = Sets.newHashSet(LoreLoader.LOADED_LORE);
            List<Lore> lores = Serializers.getStdGson().fromJson(message.getLoreJson(), new TypeToken<List<Lore>>() {}.getType());
            LoreLoader.registerLore(lores, false);
            return null;
        }
    }
}
