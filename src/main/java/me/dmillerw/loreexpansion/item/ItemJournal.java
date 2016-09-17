package me.dmillerw.loreexpansion.item;

import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemJournal extends Item {

    public ItemJournal() {
        setUnlocalizedName(LoreExpansion.ID + ".journal");
        setCreativeTab(LoreExpansion.TAB_LORE);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        player.openGui(LoreExpansion.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.openGui(LoreExpansion.INSTANCE, 0, world, 0, 0, 0);
        return super.onItemRightClick(stack, world, player, hand);
    }
}
