package me.dmillerw.loreexpansion.util;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.saving.LoreSaveData;
import me.dmillerw.loreexpansion.network.MessageOverlayLore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class LoreUtil {

    public static void provideLore(EntityPlayer player, Lore lore) {
        if (player instanceof EntityPlayerMP) {
            LoreSaveData loreSaveData = getData(player.getEntityWorld());

            boolean hasRequirement = true;
            for (LoreKey requirement : lore.getRequirements())
                if (!loreSaveData.getDataForPlayer(player).contains(requirement))
                    hasRequirement = false;

            if (hasRequirement && loreSaveData.addData(player, lore.getKey()))
                LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageOverlayLore(new TextComponentTranslation("chat.loreexpansion.lore.added", lore.getContent().getTitle()).getFormattedText()), (EntityPlayerMP) player);
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
}
