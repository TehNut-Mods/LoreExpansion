package me.dmillerw.loreexpansion;

import me.dmillerw.loreexpansion.item.ItemJournal;
import me.dmillerw.loreexpansion.item.ItemScrap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(modid = LoreExpansion.ID)
@GameRegistry.ObjectHolder(LoreExpansion.ID)
public class RegistrarLoreExpansion {

    public static final Item LORE_JOURNAL = Items.AIR;
    public static final Item LORE_SCRAP = Items.AIR;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemJournal().setRegistryName("lore_journal"));
        event.getRegistry().register(new ItemScrap().setRegistryName("lore_scrap"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(LORE_JOURNAL, 0, new ModelResourceLocation(LORE_JOURNAL.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(LORE_JOURNAL, 1, new ModelResourceLocation(LORE_JOURNAL.getRegistryName().toString() + "_creative", "inventory"));
        ModelLoader.setCustomModelResourceLocation(LORE_SCRAP, 0, new ModelResourceLocation(LORE_SCRAP.getRegistryName(), "inventory"));
    }
}
