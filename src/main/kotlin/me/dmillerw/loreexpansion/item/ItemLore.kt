package me.dmillerw.loreexpansion.item

import me.dmillerw.loreexpansion.core.LoreLoader
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * @author dmillerw
 */
class ItemLore : Item() {

    companion object {

        lateinit var item: ItemLore

        public fun register() {
            item = ItemLore().setUnlocalizedName("lore") as ItemLore
            GameRegistry.registerItem(item, "lore")
        }
    }

    init {
        setHasSubtypes(true)
        setMaxDamage(0)
        setMaxStackSize(1)
        setCreativeTab(CreativeTabs.tabMisc)
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        val tag = stack?.tagCompound
        val lore = tag?.getString("lore")

        if (lore != null && !lore.isEmpty())
            tooltip?.add("Category: " + LoreLoader.getLore(lore).category)

        super.addInformation(stack, playerIn, tooltip, advanced)
    }

    override fun getSubItems(itemIn: Item?, tab: CreativeTabs?, subItems: MutableList<ItemStack>?) {
        for (lore in LoreLoader.loadedLore) {
            val stack = ItemStack(this)
            val tag = NBTTagCompound()
            tag.setString("lore", lore.id)
            stack.tagCompound = tag
            subItems?.add(stack)
        }
    }

    override fun getItemStackDisplayName(stack: ItemStack?): String? {
        val tag = stack?.tagCompound
        val lore = tag?.getString("lore")

        if (lore != null && !lore.isEmpty())
            return "Lore: " + LoreLoader.getLore(lore).content.title

        return super.getItemStackDisplayName(stack)
    }
}