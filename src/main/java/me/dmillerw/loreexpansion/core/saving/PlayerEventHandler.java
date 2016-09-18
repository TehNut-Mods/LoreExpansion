package me.dmillerw.loreexpansion.core.saving;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onPlayerSpawn(LivingSpawnEvent event) {
        if (event.getWorld().isRemote)
            return;

        if (event.getEntityLiving() instanceof EntityPlayer) {
            LoreSaveData loreSaveData = LoreUtil.getData(event.getWorld());
            loreSaveData.initPlayer((EntityPlayer) event.getEntityLiving());
        }
    }
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntityPlayer().getEntityWorld().isRemote)
            return;

        LoreSaveData loreSaveData = LoreUtil.getData(event.getEntityPlayer().getEntityWorld());
        loreSaveData.initPlayer(event.getEntityPlayer());
    }

    @SubscribeEvent
    public void playerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP)
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
    }

}
