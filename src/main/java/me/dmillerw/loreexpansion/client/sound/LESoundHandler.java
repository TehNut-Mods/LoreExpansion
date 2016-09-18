package me.dmillerw.loreexpansion.client.sound;

import com.google.common.collect.Sets;
import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.net.URL;
import java.util.Set;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class LESoundHandler {

    public static final LESoundHandler INSTANCE = new LESoundHandler();

    private static final String[] SOUND_MANAGER_MAPPING = new String[]{"sndManager", "field_147694_f"};
    private static final String[] SOUND_SYSTEM_MAPPING = new String[]{"sndSystem", "field_148620_e"};

    private static boolean gamePause = false;

    private Set<String> isLoaded = Sets.newHashSet();

    private SoundManager soundManager;

    private SoundSystem soundSystem;

    private String nowPlaying = "";

    private boolean paused = false;
    private boolean loaded = false;

    private void initialize() {
        try {
            if (!LoreExpansion.audioDir.exists())
                LoreExpansion.audioDir.mkdirs();
            soundManager = (SoundManager) ReflectionHelper.findField(SoundHandler.class, SOUND_MANAGER_MAPPING).get(Minecraft.getMinecraft().getSoundHandler());
            soundSystem = (SoundSystem) ReflectionHelper.findField(SoundManager.class, SOUND_SYSTEM_MAPPING).get(soundManager);
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
        if (!nowPlaying.isEmpty()) {
            stop();
        }

        try {
            File file = getFile(name);
            URL url = file.toURI().toURL();

            if (!isLoaded.contains(name)) {
                getSoundSystem().newStreamingSource(true, name, url, file.getName(), false, 0F, 0F, 0F, 1, 0F);
                getSoundSystem().activate(name);
            }

            soundSystem.play(name);
            nowPlaying = name;
        } catch (Exception ex) {
            nowPlaying = "";
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (nowPlaying.isEmpty()) {
            return;
        }

        getSoundSystem().stop(nowPlaying);
//		getSoundSystem().removeSource(nowPlaying);
//		isLoaded.remove(nowPlaying);
        nowPlaying = "";
        gamePause = false;
        paused = false;
    }

    public void pause() {
        if (nowPlaying.isEmpty()) {
            return;
        }

        getSoundSystem().pause(nowPlaying);
        paused = true;
    }

    public void resume() {
        if (nowPlaying.isEmpty()) {
            return;
        }

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
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld == null && !nowPlaying.isEmpty()) {
            stop();
            return;
        }

        boolean currentState = false;
        if (Minecraft.getMinecraft().isGamePaused()) {
            currentState = true;
        }

        if (currentState && !gamePause) {
            pause();
            gamePause = true;
        } else if (!currentState && gamePause) {
            resume();
            gamePause = false;
        }
    }
}
