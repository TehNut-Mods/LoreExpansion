package me.dmillerw.loreexpansion.client;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.client.gui.GuiJournal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyHandler {

    public static final KeyBinding KEY_OPEN_JOURNAL = new KeyBinding("key." + LoreExpansion.ID + ".openJournal", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_L, LoreExpansion.NAME);

    public KeyHandler() {
        ClientRegistry.registerKeyBinding(KEY_OPEN_JOURNAL);
    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (KEY_OPEN_JOURNAL.isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new GuiJournal(false));
    }
}
