package me.dmillerw.loreexpansion.core.trigger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.SerializerBase;
import me.dmillerw.loreexpansion.core.json.Serializers;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Triggers {

    public static final BiMap<ResourceLocation, LoreTrigger<?>> LORE_TRIGGERS = HashBiMap.create();

    public static final LoreTrigger<Achievement> ACHIEVEMENT = new LoreTrigger<Achievement>() {
        @Override
        public boolean trigger(World world, EntityPlayer player, Lore lore, Achievement triggerTarget) {
            if (Serializers.ACHIEVEMENT_MAP.size() != AchievementList.ACHIEVEMENTS.size())
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
            return Serializers.ACHIEVEMENT;
        }

        @Override
        public Class<Achievement> getType() {
            return Achievement.class;
        }

        private void buildAchievements() {
            Serializers.ACHIEVEMENT_MAP.clear();
            for (Achievement achievement : AchievementList.ACHIEVEMENTS)
                Serializers.ACHIEVEMENT_MAP.put(achievement.statId, achievement);
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
            return Serializers.BLOCKPOS;
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
