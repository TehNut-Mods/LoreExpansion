package me.dmillerw.loreexpansion.item;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.saving.LoreSaveData;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote || !stack.hasTagCompound())
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        Lore lore = LoreLoader.getLore(stack.getTagCompound().getString("lore"));
        if (lore == null)
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        LoreUtil.provideLore(player, lore);
        return super.onItemRightClick(stack, world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        for (Lore lore : LoreLoader.LOADED_LORE) {
            ItemStack stack = new ItemStack(item);
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("lore", lore.getKey().getId());
            stack.setTagCompound(tagCompound);
            subItems.add(stack);
        }

        if (subItems.isEmpty()) {
            ItemStack stack = new ItemStack(item);
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("lore", Lore.NULL_LORE.getKey().getId());
            stack.setTagCompound(tagCompound);
            subItems.add(stack);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        if (!stack.hasTagCompound()) {
            tooltip.add(TextFormatting.RED + I18n.format("tooltip.loreexpansion.torn"));
            return;
        }

        String loreKey = stack.getTagCompound().getString("lore");
        Lore lore = LoreLoader.getLore(loreKey);
        if (lore == null)
            lore = Lore.NULL_LORE;
        tooltip.add(I18n.format("tooltip.loreexpansion.title", lore.getContent().getTitle()));
        tooltip.add(I18n.format("tooltip.loreexpansion.category", lore.getKey().getCategory()));
    }
}
