package me.dmillerw.loreexpansion.core.player

import com.google.common.collect.Sets
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties
import java.util.*

/**
 * @author dmillerw
 */
class PlayerData(val lore: HashSet<String> = Sets.newHashSet()) : IExtendedEntityProperties {

    companion object {
        const val KEY = "loreexpansion:properties"

        public fun attachProperties(player: EntityPlayer) {
            var properties = player.getExtendedProperties(KEY)
            if (properties == null) {
                player.registerExtendedProperties(KEY, properties)
            }
        }

        public fun getProperties(player: EntityPlayer) : PlayerData {
            return player.getExtendedProperties(KEY) as PlayerData
        }
    }

    override fun init(entity: Entity?, world: World?) {}

    override fun loadNBTData(compound: NBTTagCompound?) {
        if (compound == null)
            return

        for (key in compound.keySet) {
            if (compound.getBoolean(key)) {
                lore.add(key)
            }
        }
    }

    override fun saveNBTData(compound: NBTTagCompound?) {
        if (compound == null)
            return

        for (key in compound.keySet)
            compound.removeTag(key)

        for (key in lore)
            compound.setBoolean(key, true)
    }
}