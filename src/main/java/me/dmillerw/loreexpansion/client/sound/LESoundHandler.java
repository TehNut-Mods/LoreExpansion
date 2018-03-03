package me.dmillerw.loreexpansion.client.sound;

import com.google.common.collect.Sets;
import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Set;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class LESoundHandler {

    public static final LESoundHandler INSTANCE = new LESoundHandler();

    private static final String[] SOUND_MANAGER_MAPPING = new String[]{"sndManager", "field_147694_f"};
    private static final String[] SOUND_SYSTEM_MAPPING = new String[]{"sndSystem", "field_148620_e"};
    private static Field commandThreadField;
    private static boolean gamePause = false;
    private Set<String> isLoaded = Sets.newHashSet();
    private SoundSystem soundSystem;
    private String nowPlaying = "";
    private boolean paused = false;
    private boolean loaded = false;

    private void initialize() {
        try {
            if (!LoreExpansion.audioDir.exists())
                LoreExpansion.audioDir.mkdirs();
            SoundManager soundManager = ReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), SOUND_MANAGER_MAPPING);
            soundSystem = ReflectionHelper.getPrivateValue(SoundManager.class, soundManager, SOUND_SYSTEM_MAPPING);
            commandThreadField = SoundSystem.class.getDeclaredField("commandThread");
            commandThreadField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SoundSystem getSoundSystem() {
        if (!loaded) {
            initialize();
            loaded = true;
        }
        return soundSystem;
    }

    private File getFile(String name) {
        return new File(LoreExpansion.audioDir, name + ".ogg");
    }

    public void play(String name) {
        if (!nowPlaying.isEmpty())
            stop();

        try {
            File file = getFile(name);
            URL url = file.toURI().toURL();

            if (!isLoaded.contains(name)) {
                getSoundSystem().newStreamingSource(true, name, url, file.getName(), false, 0F, 0F, 0F, 1, 0F);
                getSoundSystem().activate(name);
                isLoaded.add(name);
            }

            soundSystem.play(name);
            nowPlaying = name;
        } catch (Exception ex) {
            nowPlaying = "";
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (nowPlaying.isEmpty())
            return;

        getSoundSystem().stop(nowPlaying);
        nowPlaying = "";
        gamePause = false;
        paused = false;
    }

    public void pause() {
        if (nowPlaying.isEmpty())
            return;

        getSoundSystem().pause(nowPlaying);
        paused = true;
    }

    public void resume() {
        if (nowPlaying.isEmpty())
            return;

        if (paused) {
            getSoundSystem().play(nowPlaying);
            paused = false;
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isPlaying(String name) {
        return nowPlaying.equals(name);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world == null && !INSTANCE.nowPlaying.isEmpty()) {
            INSTANCE.stop();
            return;
        }

        try {
            if (commandThreadField.get(INSTANCE.soundSystem) == null) // Fixes NPE while reloading resources
                return;

            INSTANCE.getSoundSystem().setVolume(INSTANCE.nowPlaying, Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.VOICE));
        } catch (NullPointerException | IllegalAccessException e) {
            // No-op
        }

        boolean currentState = Minecraft.getMinecraft().isGamePaused();
        if (currentState && !gamePause) {
            INSTANCE.pause();
            gamePause = true;
        } else if (!currentState && gamePause) {
            INSTANCE.resume();
            gamePause = false;
        }
    }
}
