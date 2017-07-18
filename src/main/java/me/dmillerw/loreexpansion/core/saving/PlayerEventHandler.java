package me.dmillerw.loreexpansion.core.saving;

import me.dmillerw.loreexpansion.LoreConfiguration;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.RegistrarLoreExpansion;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
            LoreUtil.checkDefaults(player);
        }

        NBTTagCompound persisted = GeneralUtil.getModPersistedTag(player, LoreExpansion.ID);
        if (LoreConfiguration.general.spawnWithJournal && !persisted.hasKey("loreexpansion-spawn")) {
            GeneralUtil.giveStackToPlayer(player, new ItemStack(RegistrarLoreExpansion.JOURNAL));
            persisted.setBoolean("loreexpansion-spawn", true);
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        if (event.player.getEntityWorld().isRemote || event.player.capabilities.isCreativeMode)
            return;
        InventoryPlayer inventoryPlayer = event.player.inventory;

        for (int i = 0; i < inventoryPlayer.getSizeInventory(); i++) {
            ItemStack stack = inventoryPlayer.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == RegistrarLoreExpansion.SCRAP) {
                Lore lore = LoreUtil.readLore(stack);
                if (lore.shouldAutoAdd() && LoreUtil.provideLore(event.player, lore))
                    inventoryPlayer.setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (event.getEntityPlayer().getEntityWorld().isRemote || event.getEntityPlayer().capabilities.isCreativeMode)
            return;
        ItemStack stack = event.getItem().getItem();

        if (stack.getItem() == RegistrarLoreExpansion.SCRAP && stack.hasTagCompound()) {
            Lore lore = LoreUtil.readLore(stack);
            if (lore.shouldAutoAdd() && LoreUtil.provideLore(event.getEntityPlayer(), lore)) {
                event.getItem().setDead();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (LoreConfiguration.general.syncLoresFromServer)
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLoreRegistry(Serializers.getStdGson().toJson(LoreLoader.LOADED_LORE)), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onLeaveServer(GuiScreenEvent.InitGuiEvent event) {
        if (MessageSyncLoreRegistry.LORE_BACKUP == null)
            return;

        if (Minecraft.getMinecraft().world != null)
            return;

        LoreLoader.registerLore(MessageSyncLoreRegistry.LORE_BACKUP, false);
        MessageSyncLoreRegistry.LORE_BACKUP = null;
    }
}
