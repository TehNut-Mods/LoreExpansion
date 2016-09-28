package me.dmillerw.loreexpansion;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class LoreConfiguration {

    public static Configuration config;

    public static String theme;
    public static boolean spawnWithJournal;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        Property themeProp = config.get(Configuration.CATEGORY_CLIENT, "theme", "default", "The theme to use for your narrative." +
                "\nThe string here will be used for finding your theme in the config folder." +
                "\neg: A string of \"tech\" will look for \"../config/loreexpansion/theme/tech/*\"" +
                "\nThe exception to this rule is the default theme which is loaded from the mod jar." +
                "\n[default: default]");
        themeProp.setRequiresMcRestart(true);
        theme = themeProp.getString();
        spawnWithJournal = config.getBoolean("spawnWithJournal", Configuration.CATEGORY_GENERAL, true, "Whether the player should initially spawn with the Journal or not.");

        config.save();
    }
}
