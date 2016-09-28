package me.dmillerw.loreexpansion.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class GeneralUtil {

    public static void giveStackToPlayer(EntityPlayer player, ItemStack stack) {
        boolean didGive = player.inventory.addItemStackToInventory(stack);
        if (didGive) {
            player.worldObj.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryContainer.detectAndSendChanges();
        }

        if (!didGive) {
            EntityItem entityitem = player.dropItem(stack, false);
            if (entityitem != null) {
                entityitem.setNoPickupDelay();
                entityitem.setOwner(player.getName());
            }
        }
    }
}
