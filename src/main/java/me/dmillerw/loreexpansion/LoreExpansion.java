package me.dmillerw.loreexpansion;

import me.dmillerw.loreexpansion.command.CommandLoreExpansion;
import me.dmillerw.loreexpansion.network.MessagePlayLore;
import me.dmillerw.loreexpansion.network.MessageSyncLore;
import me.dmillerw.loreexpansion.network.MessageSyncLoreRegistry;
import me.dmillerw.loreexpansion.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
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

@Mod(modid = LoreExpansion.ID, name = LoreExpansion.NAME, version = LoreExpansion.VERSION)
public class LoreExpansion {

    public static final String ID = "loreexpansion";
    public static final String NAME = "LoreExpansion";
    public static final String VERSION = "@VERSION@";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final SimpleNetworkWrapper NETWORK_WRAPPER = new SimpleNetworkWrapper(ID);
    public static final CreativeTabs TAB_LORE = new CreativeTabs("loreexpansion") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(RegistrarLoreExpansion.JOURNAL);
        }
    };

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

        NETWORK_WRAPPER.registerMessage(MessageSyncLore.Handler.class, MessageSyncLore.class, 0, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessagePlayLore.Handler.class, MessagePlayLore.class, 1, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageSyncLoreRegistry.Handler.class, MessageSyncLoreRegistry.class, 2, Side.CLIENT);

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
