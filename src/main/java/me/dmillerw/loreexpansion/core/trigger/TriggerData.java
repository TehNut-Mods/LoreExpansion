package me.dmillerw.loreexpansion.core.trigger;

import com.google.gson.JsonElement;
import me.dmillerw.loreexpansion.core.json.Serializers;
import net.minecraft.util.ResourceLocation;

public final class TriggerData {

    private final ResourceLocation triggerId;
    private final JsonElement targetJson;
    private transient Object target;

    public TriggerData(ResourceLocation triggerId, JsonElement targetJson) {
        this.triggerId = triggerId;
        this.targetJson = targetJson;
    }

    public ResourceLocation getTriggerId() {
        return triggerId;
    }

    public JsonElement getTargetJson() {
        return targetJson;
    }

    public Object getTarget() {
        if (target == null) {
            LoreTrigger<?> trigger = Triggers.LORE_TRIGGERS.get(getTriggerId());
            target = Serializers.getStdGson(trigger.getSerializer()).fromJson(targetJson, trigger.getType());
        }

        return target;
    }
}