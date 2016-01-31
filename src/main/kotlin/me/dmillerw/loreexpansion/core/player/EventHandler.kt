package me.dmillerw.loreexpansion.core.player

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.event.entity.living.LivingSpawnEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side

/**
 * @author dmillerw
 */
object EventHandler {

    @SubscribeEvent
    public fun onPlayerSpawn(event: LivingSpawnEvent) {
        val side = FMLCommonHandler.instance().effectiveSide
        if (side == Side.SERVER) {
            if (event.entityLiving is EntityPlayer) {
                PlayerData.attachProperties(event.entityLiving as EntityPlayer)
            }
        }
    }

    @SubscribeEvent
    public fun onPlayerClone(event: PlayerEvent.Clone) {
        val side = FMLCommonHandler.instance().effectiveSide
        if (side == Side.SERVER) {
            var temp = NBTTagCompound()
            PlayerData.getProperties(event.original).saveNBTData(temp)
            PlayerData.getProperties(event.entityPlayer).loadNBTData(temp)
        }
    }
}