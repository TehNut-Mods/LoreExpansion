package me.dmillerw.loreexpansion;

import me.dmillerw.loreexpansion.command.CommandLoreExpansion;
import me.dmillerw.loreexpansion.item.ItemJournal;
import me.dmillerw.loreexpansion.item.ItemScrap;
import me.dmillerw.loreexpansion.network.MessageOverlayLore;
import me.dmillerw.loreexpansion.network.MessagePlayLore;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import me.dmillerw.loreexpansion.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = LoreExpansion.ID, name = LoreExpansion.NAME, version = LoreExpansion.VERSION, dependencies = "required-after:Forge@[12.18.1.2092,);")
public class LoreExpansion {

    public static final String ID = "loreexpansion";
    public static final String NAME = "LoreExpansion";
    public static final String VERSION = "@VERSION@";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final SimpleNetworkWrapper NETWORK_WRAPPER = new SimpleNetworkWrapper(ID);
    public static final CreativeTabs TAB_LORE = new CreativeTabs("loreexpansion") {
        @Override
        public Item getTabIconItem() {
            return LORE_JOURNAL;
        }
    };
    public static final ItemScrap LORE_PAGE = new ItemScrap();
    public static final ItemJournal LORE_JOURNAL = new ItemJournal();

    @SidedProxy(clientSide = "me.dmillerw.loreexpansion.proxy.ClientProxy", serverSide = "me.dmillerw.loreexpansion.proxy.CommonProxy")
    public static CommonProxy PROXY;
    @Mod.Instance(ID)
    public static LoreExpansion INSTANCE;

    public static File configDir;
    public static File loreDir;
    public static File audioDir;
    public static File themeDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), ID);
        loreDir = new File(configDir, "lore");
        audioDir = new File(loreDir, "audio");
        themeDir = new File(configDir, "theme");
        LoreConfiguration.init(new File(configDir, ID + ".cfg"));

        NETWORK_WRAPPER.registerMessage(MessageSyncLore.Handler.class, MessageSyncLore.class, 0, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageOverlayLore.Handler.class, MessageOverlayLore.class, 1, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessagePlayLore.Handler.class, MessagePlayLore.class, 2, Side.CLIENT);

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

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLoreExpansion());
    }
}
