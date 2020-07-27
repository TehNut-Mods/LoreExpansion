package me.dmillerw.loreexpansion.proxy;

import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit() {

    }

    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(LoreExpansion.INSTANCE, new GuiHandler());
    }

    public void postInit() {

    }
}
