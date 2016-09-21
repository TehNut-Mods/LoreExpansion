package me.dmillerw.loreexpansion.core.trigger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.SerializerBase;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Type;
import java.util.Map;

public class Triggers {

    public static final BiMap<ResourceLocation, LoreTrigger<?>> LORE_TRIGGERS = HashBiMap.create();

    public static final LoreTrigger<Achievement> ACHIEVEMENT = new LoreTrigger<Achievement>() {
        Map<String, Achievement> achievementMap = Maps.newHashMap();

        @Override
        public boolean trigger(World world, EntityPlayer player, Lore lore, Achievement triggerTarget) {
            if (achievementMap.size() != AchievementList.ACHIEVEMENTS.size())
                buildAchievements();

            Achievement achievement = (Achievement) lore.getLoreTrigger().getTarget();
            if (achievement.equals(triggerTarget)) {
                LoreUtil.provideLore(player, lore);
                return true;
            }
            return false;
        }

        @Override
        public SerializerBase<Achievement> getSerializer() {
            return new SerializerBase<Achievement>() {
                @Override
                public Achievement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return achievementMap.get(context.<String>deserialize(json.getAsJsonObject().get("id"), String.class));
                }

                @Override
                public JsonElement serialize(Achievement src, Type typeOfSrc, JsonSerializationContext context) {
                    return context.serialize(src.statId);
                }

                @Override
                public Type getType() {
                    return Achievement.class;
                }
            };
        }

        @Override
        public Class<Achievement> getType() {
            return Achievement.class;
        }

        private void buildAchievements() {
            achievementMap.clear();
            for (Achievement achievement : AchievementList.ACHIEVEMENTS)
                achievementMap.put(achievement.statId, achievement);
        }
    };
    public static final LoreTrigger<BlockPos> BLOCKPOS = new LoreTrigger<BlockPos>() {
        @Override
        public boolean trigger(World world, EntityPlayer player, Lore lore, BlockPos triggerTarget) {
            BlockPos pos = (BlockPos) lore.getLoreTrigger().getTarget();
            if (pos.equals(triggerTarget)) {
                LoreUtil.provideLore(player, lore);
                return true;
            }
            return false;
        }

        @Override
        public SerializerBase<BlockPos> getSerializer() {
            return new SerializerBase<BlockPos>() {
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
        }

        @Override
        public Class<BlockPos> getType() {
            return BlockPos.class;
        }
    };

    static {
        LORE_TRIGGERS.put(new ResourceLocation(LoreExpansion.ID, "achievement"), ACHIEVEMENT);
        LORE_TRIGGERS.put(new ResourceLocation(LoreExpansion.ID, "location"), BLOCKPOS);
    }
}
