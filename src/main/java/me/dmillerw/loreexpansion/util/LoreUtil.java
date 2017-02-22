package me.dmillerw.loreexpansion.util;

import com.google.common.base.Strings;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.action.Actions;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.saving.LoreSaveData;
import me.dmillerw.loreexpansion.event.LoreObtainedEvent;
import me.dmillerw.loreexpansion.network.MessagePlayLore;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Set;

public class LoreUtil {

    public static boolean provideLore(EntityPlayer player, Lore lore) {
        if (lore == null || lore.isNull())
            return false;

        if (player instanceof EntityPlayerMP) {
            LoreSaveData loreSaveData = getData(player.getEntityWorld());

            boolean hasRequirement = true;
            for (LoreKey requirement : lore.getRequirements())
                if (!loreSaveData.getDataForPlayer(player).contains(requirement))
                    hasRequirement = false;

            if (!hasRequirement)
                return false;

            LoreObtainedEvent event = new LoreObtainedEvent((EntityPlayerMP) player, lore);

            if (loreSaveData.addData(player, lore.getKey()) && !MinecraftForge.EVENT_BUS.post(event)) {
                if (lore.shouldNotify())
                    player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("chat.loreexpansion.lore.added", lore.getContent().getTitle())), true);
                LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
                if (!Strings.isNullOrEmpty(lore.getContent().getAudio()) && lore.getContent().shouldAutoplay())
                    LoreExpansion.NETWORK_WRAPPER.sendTo(new MessagePlayLore(lore.getKey()), (EntityPlayerMP) player);

                if (lore.getLoreAction() != null)
                    Actions.LORE_ACTIONS.get(lore.getLoreAction().getActionId()).act(player, lore);

                return true;
            }
        }

        return false;
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

    public static void checkDefaults(EntityPlayer player) {
        LoreSaveData loreSaveData = getData(player.getEntityWorld());
        Set<LoreKey> current = loreSaveData.getDataForPlayer(player);
        boolean obtained = false;
        for (Lore lore : LoreLoader.LOADED_LORE) {
            if (lore.isDefaultLore() && !current.contains(lore.getKey())) {
                loreSaveData.addData(player, lore.getKey());
                obtained = true;
            }
        }

        if (obtained)
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
    }
}
