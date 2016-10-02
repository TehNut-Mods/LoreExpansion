package me.dmillerw.loreexpansion.proxy;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.DebugEventHandler;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.saving.PlayerEventHandler;
import me.dmillerw.loreexpansion.core.trigger.TriggerHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit() {
        LoreLoader.init(LoreExpansion.loreDir, true);

        GameRegistry.register(LoreExpansion.LORE_JOURNAL.setRegistryName("lore_journal"));
        GameRegistry.register(LoreExpansion.LORE_PAGE.setRegistryName("lore_scrap"));

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new TriggerHandler());
        MinecraftForge.EVENT_BUS.register(new DebugEventHandler());
    }

    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(LoreExpansion.INSTANCE, new GuiHandler());
    }

    public void postInit() {

    }
}
