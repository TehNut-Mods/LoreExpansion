package me.dmillerw.loreexpansion.core.trigger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.SerializerBase;
import me.dmillerw.loreexpansion.core.json.Serializers;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Collection;
import java.util.Map;

public class Triggers {

    public static final BiMap<ResourceLocation, LoreTrigger<?>> LORE_TRIGGERS = HashBiMap.create();

    public static final LoreTrigger<Advancement> ADVANCEMENT = new LoreTrigger<Advancement>() {
        @Override
        public boolean trigger(World world, EntityPlayer player, Lore lore, Advancement triggerTarget) {
            updateAdvancements();

            Advancement advancement = (Advancement) lore.getLoreTrigger().getTarget();
            if (advancement.equals(triggerTarget)) {
                LoreUtil.provideLore(player, lore);
                return true;
            }
            return false;
        }

        @Override
        public SerializerBase<Advancement> getSerializer() {
            return Serializers.ADVANCEMENT;
        }

        @Override
        public Class<Advancement> getType() {
            return Advancement.class;
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
        LORE_TRIGGERS.put(new ResourceLocation(LoreExpansion.ID, "achievement"), ADVANCEMENT);
        LORE_TRIGGERS.put(new ResourceLocation(LoreExpansion.ID, "location"), BLOCKPOS);
    }

    public static void updateAdvancements() {
        try {
            AdvancementList advancementList = ReflectionHelper.getPrivateValue(AdvancementManager.class, null, "field_192784_c", "ADVANCEMENT_LIST");
            Map<ResourceLocation, Advancement> registry = ReflectionHelper.getPrivateValue(AdvancementList.class, advancementList, "field_192092_b", "advancements");
            if (Serializers.ADVANCEMENT_MAP.size() != registry.size())
                buildAchievements(registry.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buildAchievements(Collection<Advancement> advancements) {
        Serializers.ADVANCEMENT_MAP.clear();
        for (Advancement advancement : advancements)
            Serializers.ADVANCEMENT_MAP.put(advancement.getId(), advancement);
    }
}
