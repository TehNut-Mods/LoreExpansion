package me.dmillerw.loreexpansion;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LoreExpansion.ID, name = LoreExpansion.ID + "/" + LoreExpansion.ID, category = "")
@Mod.EventBusSubscriber(modid = LoreExpansion.ID)
public class LoreConfiguration {

    public static Client client = new Client();
    public static General general = new General();

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(LoreExpansion.ID))
            ConfigManager.sync(LoreExpansion.ID, Config.Type.INSTANCE);
    }

    public static class Client {
        @Config.Comment({ "The theme to use for your narrative.", "The string here will be used for finding your theme in the config folder.", "eg: A string of \"tech\" will look for \"../config/loreexpansion/theme/tech/*\"", "The exception to this rule is the default theme which is loaded from the mod jar." })
        @Config.RequiresMcRestart
        public String theme = "default";
    }

    public static class General {
        @Config.Comment({ "Whether the player should initially spawn with the Journal or not." })
        public boolean spawnWithJournal = true;
        @Config.Comment({ "Syncs the Lore list when the client connects to the server.", "This setting only has an effect on the server side." })
        public boolean syncLoresFromServer = false;
    }
}
