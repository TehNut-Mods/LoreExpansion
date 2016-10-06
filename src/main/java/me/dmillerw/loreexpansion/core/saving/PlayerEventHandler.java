package me.dmillerw.loreexpansion.core.saving;

import com.google.common.collect.Sets;
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
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class PlayerEventHandler {

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
            LoreUtil.checkDefaults(player);
        }

        if (LoreConfiguration.spawnWithJournal && !player.getEntityData().hasKey("loreexpansion-spawn")) {
            GeneralUtil.giveStackToPlayer(player, new ItemStack(LoreExpansion.LORE_JOURNAL));
            player.getEntityData().setBoolean("loreexpansion-spawn", true);
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
            if (inventoryPlayer.getStackInSlot(i) != null && inventoryPlayer.getStackInSlot(i).getItem() == LoreExpansion.LORE_PAGE) {
                Lore lore = LoreUtil.readLore(inventoryPlayer.getStackInSlot(i));
                if (!lore.isNull() && lore.shouldAutoAdd() && !LoreUtil.getData(event.player.getEntityWorld()).getDataForPlayer(event.player).contains(lore.getKey())) {
                    LoreUtil.provideLore(event.player, lore);
                    inventoryPlayer.setInventorySlotContents(i, null);
                }
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
            if (!lore.isNull() && lore.shouldAutoAdd() && !LoreUtil.getData(event.getEntityPlayer().getEntityWorld()).getDataForPlayer(event.getEntityPlayer()).contains(lore.getKey())) {
                LoreUtil.provideLore(event.getEntityPlayer(), lore);
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

        Set<Class<? extends GuiScreen>> menuScreens = Sets.newHashSet(
                GuiMainMenu.class,
                GuiScreenServerList.class,
                GuiWorldSelection.class
        );

        if (Minecraft.getMinecraft().theWorld != null || !menuScreens.contains(event.getGui().getClass()))
            return;

        LoreLoader.registerLore(MessageSyncLoreRegistry.LORE_BACKUP, false);
        MessageSyncLoreRegistry.LORE_BACKUP = null;
    }
}
