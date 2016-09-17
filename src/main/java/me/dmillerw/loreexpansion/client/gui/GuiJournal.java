package me.dmillerw.loreexpansion.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.dmillerw.loreexpansion.client.texture.SubTexture;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiJournal extends GuiScreen {

    private static final ResourceLocation JOURNAL_LEFT = new ResourceLocation("loreexpansion:textures/gui/journal_left.png");
    private static final ResourceLocation JOURNAL_RIGHT = new ResourceLocation("loreexpansion:textures/gui/journal_right.png");

    private static final SubTexture PAGE_LEFT = new SubTexture(JOURNAL_LEFT, 0, 0, 175, 230);
    private static final SubTexture PAGE_RIGHT = new SubTexture(JOURNAL_RIGHT, 0, 0, 168, 230);
    private static final SubTexture LORE_BOX_SELECTED = new SubTexture(JOURNAL_LEFT, 185, 45, 18, 18);
    private static final SubTexture LORE_BOX_ACTIVE = new SubTexture(JOURNAL_LEFT, 185, 68, 18, 18);
    private static final SubTexture ARROW_DIMENSION_BACK = new SubTexture(JOURNAL_LEFT, 191, 162, 7, 8);
    private static final SubTexture ARROW_DIMENSION_BACK_MOUSEOVER = new SubTexture(JOURNAL_LEFT, 191, 175, 7, 8);
    private static final SubTexture ARROW_DIMENSION_FORWARD = new SubTexture(JOURNAL_LEFT, 205, 162, 7, 8);
    private static final SubTexture ARROW_DIMENSION_FORWARD_MOUSEOVER = new SubTexture(JOURNAL_LEFT, 205, 175, 7, 8);
    private static final SubTexture ARROW_PAGE_BACK = new SubTexture(JOURNAL_LEFT, 186, 191, 12, 12);
    private static final SubTexture ARROW_PAGE_BACK_MOUSEOVER = new SubTexture(JOURNAL_LEFT, 186, 207, 12, 12);
    private static final SubTexture ARROW_PAGE_FORWARD = new SubTexture(JOURNAL_LEFT, 205, 191, 12, 12);
    private static final SubTexture ARROW_PAGE_FORWARD_MOUSEOVER = new SubTexture(JOURNAL_LEFT, 205, 207, 12, 12);
    private static final SubTexture ARROW_SCROLL_UP = new SubTexture(JOURNAL_RIGHT, 168, 37, 13, 6);
    private static final SubTexture ARROW_SCROLL_DOWN = new SubTexture(JOURNAL_RIGHT, 168, 203, 13, 6);
    private static final SubTexture PLAY_BUTTON_ACTIVE = new SubTexture(JOURNAL_RIGHT, 177, 191, 4, 7);
    private static final SubTexture PLAY_BUTTON_INACTIVE = new SubTexture(JOURNAL_RIGHT, 177, 184, 4, 7);
    private static final SubTexture PAUSE_BUTTON_ACTIVE = new SubTexture(JOURNAL_RIGHT, 183, 192, 5, 5);
    private static final SubTexture PAUSE_BUTTON_INACTIVE = new SubTexture(JOURNAL_RIGHT, 177, 185, 5, 5);
    private static final SubTexture STOP_BUTTON_ACTIVE = new SubTexture(JOURNAL_RIGHT, 170, 192, 5, 5);
    private static final SubTexture STOP_BUTTON_INACTIVE = new SubTexture(JOURNAL_RIGHT, 170, 185, 5, 5);

    private static final Pair<Integer, Integer> LEFT_SIZE = Pair.of(175, 230);
    private static final Pair<Integer, Integer> RIGHT_SIZE = Pair.of(168, 230);
    private static final Pair<Integer, Integer> TOTAL_SIZE = Pair.of(LEFT_SIZE.getLeft() + RIGHT_SIZE.getLeft(), 230);
    private static final Pair<Integer, Integer> BOX_START = Pair.of(35, 45);
    private static final Pair<Integer, Integer> TAB_SIZE = Pair.of(15, 26);
    private static final Pair<Integer, Integer> TAB_BACK = Pair.of(0, 16);
    private static final Pair<Integer, Integer> TAB_FORWARD = Pair.of(0, 46);
    private static final Pair<Integer, Integer> ARROW_DIMENSION_BACK_POS = Pair.of(5, 25);
    private static final Pair<Integer, Integer> ARROW_DIMENSION_FORWARD_POS = Pair.of(5, 55);
    private static final Pair<Integer, Integer> ARROW_PAGE_SIZE = Pair.of(12, 12);
    private static final Pair<Integer, Integer> ARROW_PAGE_BACK_POS = Pair.of(20, 206);
    private static final Pair<Integer, Integer> ARROW_PAGE_FORWARD_POS = Pair.of(148, 206);
    private static final Pair<Integer, Integer> PLAY_POS = Pair.of(122, 203);
    private static final Pair<Integer, Integer> PAUSE_POS = Pair.of(122, 204);
    private static final Pair<Integer, Integer> STOP_POS = Pair.of(41, 204);

    private static final float TEXT_SCALE = 1.0F;
    private static final int INDENTATION = 3;
    private static final int TITLE_Y = 24;
    private static final int BODY_X = 20;
    private static final int BODY_y = 35;
    private static final int LORE_BOX_GAP = 23;
    private static final int LORE_ROW_COUNT = 17;
    private static final int ARROW_SCROLL_X = 78;
    private static final int ARROW_SCROLL_UP_Y = 37;
    private static final int ARROW_SCROLL_DOWN_Y = 203;

    public static int maxPage = 0;
    public static int loreScrollIndex = 0;
    public static int textScrollIndex = 0;

    public static List<LoreKey> playerLore = Lists.newArrayList();
    public static LoreKey selectedLore;

    private static Map<String, String> prettyCatCache = Maps.newHashMap();
    private static int categoryIndex;
    private static String currentCategory = "global";
    private static Lore currentLore;

    private List<String> currentLoreText = new ArrayList<String>();
    private EntityPlayer player;

    public GuiJournal(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void initGui() {
        changeCategory("GENERAL");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int left = (this.width - TOTAL_SIZE.getLeft()) / 2;
        int top = (this.height - TOTAL_SIZE.getRight()) / 2;

        Lore current = null;
        if (selectedLore != null) {
            current = LoreLoader.getLore(selectedLore);
            if (current == null)
                selectedLore = null;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        PAGE_LEFT.draw(0, left, top);
        PAGE_RIGHT.draw(0, left + LEFT_SIZE.getLeft(), top);

        Set<Lore> all = LoreLoader.getLoreForCategory(currentCategory);
        int index = 1;
        for (Lore lore : all) {
            if (lore != null && !lore.isNull()) {
                int page = Math.min(index, 35);
                index++;

                if (page > 0 && page < 35) {
                    int drawX = (page - 1) % 5 * 23;
                    int drawY = 0;

                    if (page - 1 > 4)
                        drawY = (page - 1) / 5 * 23;

                    if (selectedLore != null && selectedLore.equals(lore.getKey()))
                        LORE_BOX_ACTIVE.draw(1, left + BOX_START.getLeft() + drawX, top + BOX_START.getRight() + drawY);
                    else
                        LORE_BOX_SELECTED.draw(1, left + BOX_START.getLeft() + drawX, top + BOX_START.getRight() + drawY);
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_E || keyCode == Keyboard.KEY_ESCAPE)
            Minecraft.getMinecraft().displayGuiScreen(null);
    }

    public void reset() {
        maxPage = LoreLoader.getLoreForCategory(currentCategory).size();
        selectedLore = null;
        textScrollIndex = 0;
        this.currentLoreText.clear();
//        SoundHandler.INSTANCE.stop();
    }

    public void changeCategory(String category) {
        currentCategory = category;
        categoryIndex = LoreLoader.categories.indexOf(currentCategory);
        reset();
    }
}