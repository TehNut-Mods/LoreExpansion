package me.dmillerw.loreexpansion.item;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.json.data.Lore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemScrap extends Item {

    public ItemScrap() {
        setUnlocalizedName(LoreExpansion.ID + ".scrap");
        setCreativeTab(LoreExpansion.TAB_LORE);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        for (Lore lore : LoreLoader.LOADED_LORE) {
            ItemStack stack = new ItemStack(item);
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("lore", lore.getId());
            stack.setTagCompound(tagCompound);
            subItems.add(stack);
        }

        if (subItems.isEmpty()) {
            ItemStack stack = new ItemStack(item);
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("lore", Lore.NULL_LORE.getId());
            stack.setTagCompound(tagCompound);
            subItems.add(stack);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (!stack.hasTagCompound())
            return;

        String loreKey = stack.getTagCompound().getString("lore");
        Lore lore = LoreLoader.getLore(loreKey);
        if (lore == null)
            lore = Lore.NULL_LORE;
        tooltip.add("Category: " + lore.getCategory());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("lore"))
            return super.getItemStackDisplayName(stack);

        String loreKey = stack.getTagCompound().getString("lore");
        Lore lore = LoreLoader.getLore(loreKey);
        if (lore == null)
            lore = Lore.NULL_LORE;
        return "Lore: " + lore.getContent().getTitle();
    }
}
