package me.dmillerw.loreexpansion.core.saving;

import me.dmillerw.loreexpansion.LoreConfiguration;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.Serializers;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import me.dmillerw.loreexpansion.network.MessageSyncLoreRegistry;
import me.dmillerw.loreexpansion.util.GeneralUtil;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerEventHandler {

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
            LoreUtil.checkDefaults(player);
        }

        NBTTagCompound persisted = getModPersistedTag(player, LoreExpansion.ID);
        if (LoreConfiguration.spawnWithJournal && !persisted.hasKey("loreexpansion-spawn")) {
            GeneralUtil.giveStackToPlayer(player, new ItemStack(LoreExpansion.LORE_JOURNAL));
            persisted.setBoolean("loreexpansion-spawn", true);
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        if (event.player.getEntityWorld().isRemote || event.player.capabilities.isCreativeMode)
            return;
        InventoryPlayer inventoryPlayer = event.player.inventory;

        for (int i = 0; i < inventoryPlayer.getSizeInventory(); i++) {
            ItemStack stack = inventoryPlayer.getStackInSlot(i);
            if (stack != null && stack.getItem() == LoreExpansion.LORE_PAGE) {
                Lore lore = LoreUtil.readLore(stack);
                if (lore.shouldAutoAdd() && LoreUtil.provideLore(event.player, lore))
                    inventoryPlayer.setInventorySlotContents(i, null);
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        if (event.getEntityPlayer().getEntityWorld().isRemote || event.getEntityPlayer().capabilities.isCreativeMode)
            return;
        ItemStack stack = event.getItem().getEntityItem();

        if (stack.getItem() == LoreExpansion.LORE_PAGE && stack.hasTagCompound()) {
            Lore lore = LoreUtil.readLore(stack);
            if (lore.shouldAutoAdd() && LoreUtil.provideLore(event.getEntityPlayer(), lore)) {
                event.getItem().setDead();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (LoreConfiguration.syncLoresFromServer)
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLoreRegistry(Serializers.getStdGson().toJson(LoreLoader.LOADED_LORE)), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLeaveServer(GuiScreenEvent.InitGuiEvent event) {
        if (MessageSyncLoreRegistry.LORE_BACKUP == null)
            return;

        if (Minecraft.getMinecraft().world != null)
            return;

        LoreLoader.registerLore(MessageSyncLoreRegistry.LORE_BACKUP, false);
        MessageSyncLoreRegistry.LORE_BACKUP = null;
    }

    public NBTTagCompound getModPersistedTag(EntityPlayer player, String modid) {
        NBTTagCompound tag = player.getEntityData();

        NBTTagCompound persistTag;
        if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        else
            tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag = new NBTTagCompound());

        NBTTagCompound modTag;
        if (persistTag.hasKey(modid))
            modTag = persistTag.getCompoundTag(modid);
        else
            persistTag.setTag(modid, modTag = new NBTTagCompound());

        return modTag;
    }
}
