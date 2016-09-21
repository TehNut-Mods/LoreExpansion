package me.dmillerw.loreexpansion.core.trigger;

import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;

public class TriggerHandler {

    @SubscribeEvent
    public void onAchievement(AchievementEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.getEntityWorld();
        if (world.isRemote || player.hasAchievement(event.getAchievement()))
            return;
        runTrigger(Triggers.ACHIEVEMENT, player, event.getAchievement());
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.getEntityWorld();
        if (world.isRemote)
            return;
        runTrigger(Triggers.BLOCKPOS, player, player.getPosition());
    }

    @SuppressWarnings("unchecked")
    private <T> void runTrigger(LoreTrigger triggerType, EntityPlayer player, T type) {
        Set<LoreKey> discovered = LoreUtil.getData(player.getEntityWorld()).getDataForPlayer(player);
        for (Lore lore : LoreLoader.LOADED_LORE) {
            if (discovered.contains(lore.getKey()) || lore.getLoreTrigger() == null)
                return;

            LoreTrigger<?> loreTrigger = Triggers.LORE_TRIGGERS.get(lore.getLoreTrigger().getTriggerId());
            if (loreTrigger != null && loreTrigger == triggerType)
                triggerType.<T>trigger(player.getEntityWorld(), player, lore, type);
        }
    }
}
