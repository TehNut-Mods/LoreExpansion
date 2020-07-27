package me.dmillerw.loreexpansion.item;

import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemJournal extends Item {

    public ItemJournal() {
        setTranslationKey(LoreExpansion.ID + ".journal");
        setCreativeTab(LoreExpansion.TAB_LORE);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (!isInCreativeTab(tab))
            return;

        subItems.add(new ItemStack(this, 1, 0));
        subItems.add(new ItemStack(this, 1, 1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        player.openGui(LoreExpansion.INSTANCE, stack.getItemDamage(), world, 0, 0, 0);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        if (stack.getItemDamage() == 1)
            return EnumRarity.UNCOMMON;

        return super.getRarity(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.getItemDamage() == 1)
            tooltip.add(TextFormatting.GOLD + I18n.format("tooltip.loreexpansion.creatve"));
    }
}
