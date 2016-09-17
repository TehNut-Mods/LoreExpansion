package me.dmillerw.loreexpansion.core.saving;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onPlayerSpawn(LivingSpawnEvent event) {
        if (event.getWorld().isRemote)
            return;

        if (event.getEntityLiving() instanceof EntityPlayer) {
            LoreSaveData loreSaveData = getData(event.getWorld());
            loreSaveData.initPlayer((EntityPlayer) event.getEntityLiving());
        }
    }
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntityPlayer().getEntityWorld().isRemote)
            return;

        LoreSaveData loreSaveData = getData(event.getEntityPlayer().getEntityWorld());
        loreSaveData.initPlayer(event.getEntityPlayer());
    }

    @SubscribeEvent
    public void playerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP)
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
    }

    public static LoreSaveData getData(World world) {
        LoreSaveData loreSaveData = (LoreSaveData) world.getMapStorage().getOrLoadData(LoreSaveData.class, LoreSaveData.LORE_DATA_ID.toString());
        if (loreSaveData == null) {
            loreSaveData = new LoreSaveData();
            world.getMapStorage().setData(LoreSaveData.LORE_DATA_ID.toString(), new LoreSaveData());
        }

        return loreSaveData;
    }
}
