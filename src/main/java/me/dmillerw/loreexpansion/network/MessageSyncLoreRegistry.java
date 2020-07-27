package me.dmillerw.loreexpansion.network;

import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreCachedData;
import me.dmillerw.loreexpansion.core.json.Serializers;
import me.dmillerw.loreexpansion.core.loader.LoreManager;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class MessageSyncLoreRegistry implements IMessage {

    public static LoreCachedData LORE_BACKUP;

    private List<Lore> lores;

    public MessageSyncLoreRegistry() {

    }

    public MessageSyncLoreRegistry(List<Lore> lores) {
        this.lores = lores;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        Serializers.getStdGson().fromJson(ByteBufUtils.readUTF8String(buf), new TypeToken<List<Lore>>(){}.getType());

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, Serializers.getStdGson().toJson(lores));
    }

    public static class Handler implements IMessageHandler<MessageSyncLoreRegistry, IMessage> {
        @Override
        public IMessage onMessage(MessageSyncLoreRegistry message, MessageContext ctx) {
            ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
                LORE_BACKUP = LoreManager.LORES.get(LoreExpansion.ID).get(0);
                LoreManager.LORES.get(LoreExpansion.ID).set(0, new LoreCachedData(message.lores));
                return false;
            });
            return null;
        }
    }
}
