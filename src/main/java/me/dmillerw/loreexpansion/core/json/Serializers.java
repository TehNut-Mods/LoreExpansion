package me.dmillerw.loreexpansion.core.json;

import com.google.common.collect.Sets;
import com.google.gson.*;
import me.dmillerw.loreexpansion.core.data.Content;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.trigger.TriggerData;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

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

    public static final SerializerBase<Lore> LORE = new SerializerBase<Lore>() {
        @Override
        public Lore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            LoreKey loreKey = context.deserialize(json.getAsJsonObject().get("key"), LoreKey.class);
            Content content = context.deserialize(json.getAsJsonObject().get("content"), Content.class);
            int sortingIndex = json.getAsJsonObject().get("sortingIndex").getAsInt();
            LoreKey[] requirements = context.deserialize(json.getAsJsonObject().get("requirements").getAsJsonArray(), LoreKey[].class);
            TriggerData loreTrigger = context.deserialize(json.getAsJsonObject().get("trigger"), TriggerData.class);
            return new Lore(loreKey, content, sortingIndex, Sets.newHashSet(requirements), loreTrigger);
        }

        @Override
        public JsonElement serialize(Lore src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("key", context.serialize(src.getKey()));
            jsonObject.add("content", context.serialize(src.getContent()));
            jsonObject.addProperty("sortingIndex", src.getSortingIndex());
            jsonObject.add("requirements", context.serialize(src.getRequirements()));
            jsonObject.add("trigger", context.serialize(src.getLoreTrigger()));
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
            jsonObject.addProperty("domain", src.getResourceDomain());
            jsonObject.addProperty("path", src.getResourcePath());
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
}
