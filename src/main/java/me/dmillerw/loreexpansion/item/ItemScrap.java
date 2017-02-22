package me.dmillerw.loreexpansion.item;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.util.LoreUtil;
import me.dmillerw.loreexpansion.util.StringHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote || !stack.hasTagCompound())
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        Lore lore = LoreUtil.readLore(stack);
        if (lore == null || lore.isNull())
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        LoreUtil.provideLore(player, lore);
        if (!player.capabilities.isCreativeMode)
            stack.shrink(1);
        return super.onItemRightClick(world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (String category : LoreLoader.getCategories())
            for (Lore lore : LoreLoader.getLoreForCategory(category))
                if (!lore.isHidden())
                    subItems.add(LoreUtil.attachLore(new ItemStack(item), lore.getKey()));

        if (subItems.isEmpty())
            subItems.add(LoreUtil.attachLore(new ItemStack(item), Lore.NULL_LORE.getKey()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        if (!stack.hasTagCompound()) {
            tooltip.add(TextFormatting.RED + I18n.format("tooltip.loreexpansion.torn"));
            return;
        }

        Lore lore = LoreUtil.readLore(stack);
        if (lore == null)
            lore = Lore.NULL_LORE;

        tooltip.add(I18n.format("tooltip.loreexpansion.title", StringHelper.getLocalizedText(lore.getContent().getTitle())));
        tooltip.add(I18n.format("tooltip.loreexpansion.category", StringHelper.getLocalizedText(lore.getKey().getCategory())));
    }
}
