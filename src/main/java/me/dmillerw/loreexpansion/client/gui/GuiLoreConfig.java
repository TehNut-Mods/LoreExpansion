package me.dmillerw.loreexpansion.client.gui;

import me.dmillerw.loreexpansion.LoreConfiguration;
import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GuiLoreConfig extends GuiConfig {

    public GuiLoreConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), LoreExpansion.ID, false, false, LoreExpansion.NAME);
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new ConfigElement(LoreConfiguration.config.getCategory(Configuration.CATEGORY_GENERAL.toLowerCase(Locale.ENGLISH))));
        list.add(new ConfigElement(LoreConfiguration.config.getCategory(Configuration.CATEGORY_CLIENT.toLowerCase(Locale.ENGLISH))));
        return list;
    }
}
