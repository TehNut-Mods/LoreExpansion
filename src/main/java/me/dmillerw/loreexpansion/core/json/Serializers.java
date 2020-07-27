package me.dmillerw.loreexpansion.core.json;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.action.ActionData;
import me.dmillerw.loreexpansion.core.data.Content;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.trigger.TriggerData;
import net.minecraft.advancements.Advancement;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Map;

public class Serializers {

    @Nonnull
    public static Gson getGson(SerializerBase<?>... serializers) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.disableHtmlEscaping();
        for (SerializerBase<?> serializer : serializers)
            gsonBuilder.registerTypeAdapter(serializer.getType(), serializer);
        return gsonBuilder.create();
    }
    public static Gson getStdGson(SerializerBase<?>... serializers) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.disableHtmlEscaping();
        for (SerializerBase<?> serializer : serializers)
            gsonBuilder.registerTypeAdapter(serializer.getType(), serializer);
        for (SerializerBase<?> serializer : ALL_SERIALIZERS)
            gsonBuilder.registerTypeAdapter(serializer.getType(), serializer);
        return gsonBuilder.create();
    }

    public static final Map<ResourceLocation, Advancement> ADVANCEMENT_MAP = Maps.newHashMap();
    public static final SerializerBase<Lore> LORE = new SerializerBase<Lore>() {
        @Override
        public Lore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            LoreKey loreKey = context.deserialize(json.getAsJsonObject().get("key"), LoreKey.class);
            Content content = context.deserialize(json.getAsJsonObject().get("content"), Content.class);
            int sortingIndex = json.getAsJsonObject().get("sortingIndex").getAsInt();
            LoreKey[] requirements = context.deserialize(json.getAsJsonObject().get("requirements").getAsJsonArray(), LoreKey[].class);
            boolean autoAdd = true;
            if (json.getAsJsonObject().has("autoAdd"))
                autoAdd = json.getAsJsonObject().get("autoAdd").getAsBoolean();
            boolean defaultLore = false;
            if (json.getAsJsonObject().has("defaultLore"))
                defaultLore = json.getAsJsonObject().get("defaultLore").getAsBoolean();
            boolean notify = true;
            if (json.getAsJsonObject().has("notify"))
                notify = json.getAsJsonObject().get("notify").getAsBoolean();
            boolean hidden = false;
            if (json.getAsJsonObject().has("hidden"))
                hidden = json.getAsJsonObject().get("hidden").getAsBoolean();
            TriggerData loreTrigger = context.deserialize(json.getAsJsonObject().get("trigger"), TriggerData.class);
            ActionData actionData = context.deserialize(json.getAsJsonObject().get("action"), ActionData.class);
            return new Lore(loreKey, content, sortingIndex, Sets.newHashSet(requirements), autoAdd, defaultLore, notify, loreTrigger, actionData).setHidden(hidden);
        }

        @Override
        public JsonElement serialize(Lore src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("key", context.serialize(src.getKey()));
            jsonObject.add("content", context.serialize(src.getContent()));
            jsonObject.addProperty("sortingIndex", src.getSortingIndex());
            jsonObject.add("requirements", context.serialize(src.getRequirements()));
            jsonObject.addProperty("autoAdd", src.shouldAutoAdd());
            jsonObject.addProperty("defaultLore", src.isDefaultLore());
            jsonObject.add("trigger", context.serialize(src.getLoreTrigger()));
            jsonObject.add("action", context.serialize(src.getLoreAction()));
            return jsonObject;
        }

        @Override
        public Type getType() {
            return Lore.class;
        }
    };
    public static final SerializerBase<ResourceLocation> RESOURCE_LOCATION = new SerializerBase<ResourceLocation>() {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String domain = json.getAsJsonObject().get("domain").getAsString();
            String path = json.getAsJsonObject().get("path").getAsString();
            return new ResourceLocation(domain, path);
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("domain", src.getNamespace());
            jsonObject.addProperty("path", src.getPath());
            return jsonObject;
        }

        @Override
        public Type getType() {
            return ResourceLocation.class;
        }
    };
    public static final SerializerBase<TriggerData> TRIGGER_DATA = new SerializerBase<TriggerData>() {
        @Override
        public TriggerData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ResourceLocation triggerId = context.deserialize(json.getAsJsonObject().get("triggerId"), ResourceLocation.class);
            JsonElement jsonElement = json.getAsJsonObject().get("target");
            return new TriggerData(triggerId, jsonElement);
        }

        @Override
        public JsonElement serialize(TriggerData src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("triggerId", context.serialize(src.getTriggerId()));
            jsonObject.add("target", src.getTargetJson());
            return jsonObject;
        }

        @Override
        public Type getType() {
            return TriggerData.class;
        }
    };
    public static final SerializerBase<ActionData> ACTION_DATA = new SerializerBase<ActionData>() {
        @Override
        public ActionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ResourceLocation actionId = context.deserialize(json.getAsJsonObject().get("actionId"), ResourceLocation.class);
            JsonElement data = json.getAsJsonObject().get("data");
            return new ActionData(actionId, data);
        }

        @Override
        public JsonElement serialize(ActionData src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("actionId", context.serialize(src.getActionId()));
            jsonObject.add("data", src.getActionJson());
            return jsonObject;
        }

        @Override
        public Type getType() {
            return ActionData.class;
        }
    };
    public static final SerializerBase<Advancement> ADVANCEMENT = new SerializerBase<Advancement>() {
        @Override
        public Advancement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ADVANCEMENT_MAP.get(context.<String>deserialize(json.getAsJsonObject().get("id"), String.class));
        }

        @Override
        public JsonElement serialize(Advancement src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.getId());
        }

        @Override
        public Type getType() {
            return Advancement.class;
        }
    };
    public static final SerializerBase<BlockPos> BLOCKPOS = new SerializerBase<BlockPos>() {
        @Override
        public BlockPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            int x = json.getAsJsonObject().get("xPos").getAsInt();
            int y = json.getAsJsonObject().get("yPos").getAsInt();
            int z = json.getAsJsonObject().get("zPos").getAsInt();
            return new BlockPos(x, y, z);
        }

        @Override
        public JsonElement serialize(BlockPos src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("xPos", src.getX());
            jsonObject.addProperty("yPos", src.getY());
            jsonObject.addProperty("zPos", src.getZ());
            return jsonObject;
        }

        @Override
        public Type getType() {
            return BlockPos.class;
        }
    };
    public static final SerializerBase<ItemStack> ITEMSTACK = new SerializerBase<ItemStack>() {
        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ResourceLocation registryName = context.deserialize(json.getAsJsonObject().get("id"), ResourceLocation.class);
            int amount = 1;
            if (json.getAsJsonObject().has("amount"))
                amount = json.getAsJsonObject().get("amount").getAsInt();
            int meta = 0;
            if (json.getAsJsonObject().has("meta"))
                meta = json.getAsJsonObject().get("meta").getAsInt();
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(registryName), amount, meta);
            try {
                if (json.getAsJsonObject().has("nbt"))
                    stack.setTagCompound(JsonToNBT.getTagFromJson(json.getAsJsonObject().get("nbt").getAsString()));
            } catch (Exception e) {
                LoreExpansion.LOGGER.error("Error parsing NBT JSON for a stack containing {}", registryName);
                LoreExpansion.LOGGER.error(e.getLocalizedMessage());
            }
            return stack;
        }

        @Override
        public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("id", context.serialize(src.getItem().getRegistryName()));
            jsonObject.addProperty("amount", src.getCount());
            jsonObject.addProperty("meta", src.getItemDamage());
            if (src.hasTagCompound())
                jsonObject.addProperty("nbt", src.getTagCompound().toString());
            return jsonObject;
        }

        @Override
        public Type getType() {
            return ItemStack.class;
        }
    };
    public static final SerializerBase[] ALL_SERIALIZERS = new SerializerBase[] {
            LORE,
            RESOURCE_LOCATION,
            TRIGGER_DATA,
            ACTION_DATA,
            BLOCKPOS,
            ADVANCEMENT,
            ITEMSTACK
    };
}
