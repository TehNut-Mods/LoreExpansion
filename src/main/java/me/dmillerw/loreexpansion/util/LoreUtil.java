package me.dmillerw.loreexpansion.util;

import com.google.common.base.Strings;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.saving.LoreSaveData;
import me.dmillerw.loreexpansion.event.LoreObtainedEvent;
import me.dmillerw.loreexpansion.network.MessageOverlayLore;
import me.dmillerw.loreexpansion.network.MessagePlayLore;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class LoreUtil {

    public static void provideLore(EntityPlayer player, Lore lore) {
        if (lore == null || lore.isNull())
            return;

        if (player instanceof EntityPlayerMP) {
            LoreSaveData loreSaveData = getData(player.getEntityWorld());

            boolean hasRequirement = true;
            for (LoreKey requirement : lore.getRequirements())
                if (!loreSaveData.getDataForPlayer(player).contains(requirement))
                    hasRequirement = false;

            LoreObtainedEvent event = new LoreObtainedEvent((EntityPlayerMP) player, lore);

            if (hasRequirement && loreSaveData.addData(player, lore.getKey()) && !MinecraftForge.EVENT_BUS.post(event)) {
                LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageOverlayLore("chat.loreexpansion.lore.added", lore.getContent().getTitle()), (EntityPlayerMP) player);
                LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
                if (!Strings.isNullOrEmpty(lore.getContent().getAudio()) && lore.getContent().shouldAutoplay())
                    LoreExpansion.NETWORK_WRAPPER.sendTo(new MessagePlayLore(lore.getKey()), (EntityPlayerMP) player);
            }
        }
    }

    public static void provideLore(EntityPlayer player, LoreKey loreKey) {
        provideLore(player, LoreLoader.getLore(loreKey));
    }

    public static LoreSaveData getData(World world) {
        LoreSaveData loreSaveData = (LoreSaveData) world.getMapStorage().getOrLoadData(LoreSaveData.class, LoreSaveData.LORE_DATA_ID.toString());
        if (loreSaveData == null) {
            loreSaveData = new LoreSaveData();
            world.getMapStorage().setData(LoreSaveData.LORE_DATA_ID.toString(), new LoreSaveData());
        }

        return loreSaveData;
    }

    public static ItemStack attachLore(ItemStack stack, LoreKey loreKey) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound loreTag = new NBTTagCompound();
        loreTag.setString("id", loreKey.getId());
        loreTag.setString("category", loreKey.getCategory());

        stack.getTagCompound().setTag("lore", loreTag);
        return stack;
    }

    public static Lore readLore(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("lore"))
            return Lore.NULL_LORE;

        NBTTagCompound loreTag = stack.getTagCompound().getCompoundTag("lore");
        LoreKey loreKey = new LoreKey(loreTag.getString("id"), loreTag.getString("category"));
        return LoreLoader.getLore(loreKey);
    }
}
