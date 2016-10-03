package me.dmillerw.loreexpansion.core.action;

import com.google.common.collect.Maps;
import com.google.gson.*;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.SerializerBase;
import me.dmillerw.loreexpansion.util.CommandSenderLore;
import me.dmillerw.loreexpansion.util.GeneralUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Map;

public class Actions {

    public static final Map<ResourceLocation, ILoreAction<?>> LORE_ACTIONS = Maps.newHashMap();

    public static final ILoreAction<String[]> COMMAND = new ILoreAction<String[]>() {
        @Override
        public void act(EntityPlayer player, Lore lore) {
            String[] commands = (String[]) lore.getLoreAction().getAction();
            for (String command : commands) {
                command = command.replaceAll("%player%", player.getName());
                int amount = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(new CommandSenderLore(player), command);
                if (amount <= 0)
                    LoreExpansion.LOGGER.error("Error executing command {}", command);
            }
        }

        @Nonnull
        @Override
        public SerializerBase<String[]> getSerializer() {
            return new SerializerBase<String[]>() {
                @Override
                public String[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonArray commands = json.getAsJsonObject().getAsJsonArray("commands");
                    String[] ret = new String[commands.size()];
                    for (int i = 0; i < commands.size(); i++)
                        ret[i] = commands.get(i).getAsString();
                    return ret;
                }

                @Override
                public JsonElement serialize(String[] src, Type typeOfSrc, JsonSerializationContext context) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("commands", context.serialize(src));
                    return jsonObject;
                }

                @Override
                public Type getType() {
                    return String[].class;
                }
            };
        }

        @Nonnull
        @Override
        public Type getType() {
            return String[].class;
        }
    };
    public static final ILoreAction<ItemStack[]> REWARD_ITEM = new ILoreAction<ItemStack[]>() {
        @Override
        public void act(EntityPlayer player, Lore lore) {
            ItemStack[] rewards = (ItemStack[]) lore.getLoreAction().getAction();
            for (ItemStack stack : rewards)
                GeneralUtil.giveStackToPlayer(player, stack);
        }

        @Nonnull
        @Override
        public SerializerBase<ItemStack[]> getSerializer() {
            return new SerializerBase<ItemStack[]>() {
                @Override
                public ItemStack[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    JsonArray rewards = json.getAsJsonObject().getAsJsonArray("rewards");
                    ItemStack[] ret = new ItemStack[rewards.size()];
                    for (int i = 0; i < rewards.size(); i++)
                        ret[i] = context.deserialize(rewards.get(i).getAsJsonObject(), ItemStack.class);
                    return ret;
                }

                @Override
                public JsonElement serialize(ItemStack[] src, Type typeOfSrc, JsonSerializationContext context) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("rewards", context.serialize(src));
                    return jsonObject;
                }

                @Override
                public Type getType() {
                    return ItemStack[].class;
                }
            };
        }

        @Nonnull
        @Override
        public Type getType() {
            return ItemStack[].class;
        }
    };

    static {
        LORE_ACTIONS.put(new ResourceLocation(LoreExpansion.ID, "command"), COMMAND);
        LORE_ACTIONS.put(new ResourceLocation(LoreExpansion.ID, "reward_item"), REWARD_ITEM);
    }
}
