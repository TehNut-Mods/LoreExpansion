package me.dmillerw.loreexpansion;

import me.dmillerw.loreexpansion.item.ItemJournal;
import me.dmillerw.loreexpansion.item.ItemScrap;
import me.dmillerw.loreexpansion.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = LoreExpansion.ID, name = LoreExpansion.NAME, version = LoreExpansion.VERSION)
public class LoreExpansion {

    public static final String ID = "loreexpansion";
    public static final String NAME = "LoreExpansion";
    public static final String VERSION = "@VERSION@";

    @SidedProxy(clientSide = "me.dmillerw.loreexpansion.proxy.ClientProxy", serverSide = "me.dmillerw.loreexpansion.proxy.CommonProxy")
    public static CommonProxy PROXY;

    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final CreativeTabs TAB_LORE = new CreativeTabs("loreexpansion") {
        @Override
        public Item getTabIconItem() {
            return LORE_JOURNAL;
        }
    };
    public static final ItemScrap LORE_PAGE = new ItemScrap();
    public static final ItemJournal LORE_JOURNAL = new ItemJournal();

    public static File loreDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loreDir = new File(event.getModConfigurationDirectory(), ID);
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit();
    }
}
