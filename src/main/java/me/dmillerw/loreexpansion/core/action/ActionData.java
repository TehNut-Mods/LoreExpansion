package me.dmillerw.loreexpansion.core.action;

import com.google.gson.JsonElement;
import me.dmillerw.loreexpansion.core.json.Serializers;
import net.minecraft.util.ResourceLocation;

public final class ActionData {

    private final ResourceLocation actionId;
    private final JsonElement actionJson;
    private transient Object action;

    public ActionData(ResourceLocation actionId, JsonElement actionJson) {
        this.actionId = actionId;
        this.actionJson = actionJson;
    }

    public ResourceLocation getActionId() {
        return actionId;
    }

    public JsonElement getActionJson() {
        return actionJson;
    }

    public Object getAction() {
        if (action == null) {
            ILoreAction<?> action = Actions.LORE_ACTIONS.get(getActionId());
            this.action = Serializers.getStdGson(action.getSerializer()).fromJson(actionJson, action.getType());
        }

        return action;
    }
}
