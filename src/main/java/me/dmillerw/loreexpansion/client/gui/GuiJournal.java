package me.dmillerw.loreexpansion.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.client.sound.LESoundHandler;
import me.dmillerw.loreexpansion.client.texture.SubTexture;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.proxy.ClientProxy;
import me.dmillerw.loreexpansion.util.StringHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;

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
    private static final Pair<Integer, Integer> TOTAL_SIZE = Pair.of(LEFT_SIZE.getLeft() + RIGHT_SIZE.getRight(), 230);
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

    private static final float TEXT_SCALE = 1F;

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
    private static String currentCategory;

    private static Lore currentLore;
    private List<String> currentLoreText = Lists.newArrayList();

    @Override
    public void initGui() {
        if (selectedLore != null) {
            LoreKey copy = selectedLore.copy();
            if (!currentCategory.equalsIgnoreCase(selectedLore.getCategory()))
                changeCategory(selectedLore.getCategory());
            changeLore(copy);
            ClientProxy.pickedUpPage = null;
        } else {
            if (currentCategory == null || currentCategory.isEmpty())
                changeCategory(Lore.GLOBAL);
        }
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        onWheelScrolled(x, y, Mouse.getDWheel());

        // VARIABLES
        int left = (width - TOTAL_SIZE.getLeft()) / 2;
        int top = (height - TOTAL_SIZE.getRight()) / 2;

        Lore current = null;
        if (selectedLore != null) {
            current = LoreLoader.getLore(selectedLore);

            if (current == null) {
                selectedLore = null;
            }
        }

        Set<Lore> all = LoreLoader.getLoreForCategory(currentCategory);

        // BACKGROUND
        GlStateManager.color(1F, 1F, 1F, 1F);
        PAGE_LEFT.draw(left, top, 0);
        PAGE_RIGHT.draw(left + LEFT_SIZE.getLeft(), top, 0);

        // LORE DRAWING
        int index = 1;
        for (Lore lore : all) {
            if (lore != null && !lore.isHidden()) {
                mc.getTextureManager().bindTexture(JOURNAL_RIGHT);

                int page = Math.min(index, 35);

                index++;

                if (page > 0 && page < 35) {
                    int drawX = (((page - 1) % 5) * LORE_BOX_GAP);
                    int drawY = 0;
                    if ((page - 1) > 4) {
                        drawY = (((page - 1) / 5) * LORE_BOX_GAP);
                    }

                    // BACKGROUND
                    if (selectedLore != null && selectedLore.equals(lore.getKey())) {
                        LORE_BOX_ACTIVE.draw(left + BOX_START.getLeft() + drawX, top + BOX_START.getRight() + drawY, (int) zLevel);
                    } else {
                        LORE_BOX_SELECTED.draw(left + BOX_START.getLeft() + drawX, top + BOX_START.getRight() + drawY, (int) zLevel);
                    }

                    // ICONS
                    if (playerLore.contains(lore.getKey()))
                        mc.getRenderItem().renderItemIntoGUI(new ItemStack(LoreExpansion.LORE_PAGE), left + BOX_START.getLeft() + drawX + 1, top + BOX_START.getRight() + drawY + 1);
                }
            }
        }

        // AUDIO CONTROL
        if (current != null && !Strings.isNullOrEmpty(current.getContent().getAudio())) {
            String sound = current.getContent().getAudio();
            if (LESoundHandler.INSTANCE.isPlaying(sound)) {
                STOP_BUTTON_ACTIVE.draw(left + LEFT_SIZE.getLeft() + STOP_POS.getLeft(), top + STOP_POS.getRight(), (int) zLevel);
                if (LESoundHandler.INSTANCE.isPaused()) {
                    PLAY_BUTTON_ACTIVE.draw(left + LEFT_SIZE.getLeft() + PLAY_POS.getLeft(), top + PLAY_POS.getRight(), (int) zLevel);
                } else {
                    PAUSE_BUTTON_ACTIVE.draw(left + LEFT_SIZE.getLeft() + PAUSE_POS.getLeft(), top + PAUSE_POS.getRight(), (int) zLevel);
                }
            } else {
                STOP_BUTTON_INACTIVE.draw(left + LEFT_SIZE.getLeft() + STOP_POS.getLeft(), top + STOP_POS.getRight(), (int) zLevel);
                PLAY_BUTTON_ACTIVE.draw(left + LEFT_SIZE.getLeft() + PLAY_POS.getLeft(), top + PLAY_POS.getRight(), (int) zLevel);
            }
        } else {
            STOP_BUTTON_INACTIVE.draw(left + LEFT_SIZE.getLeft() + STOP_POS.getLeft(), top + STOP_POS.getRight(), (int) zLevel);
            PLAY_BUTTON_INACTIVE.draw(left + LEFT_SIZE.getLeft() + PLAY_POS.getLeft(), top + PLAY_POS.getRight(), (int) zLevel);
        }

        // ARROWS - SCROLL
        GlStateManager.color(1, 1, 1, 1);
        if (current != null) {
            if (textScrollIndex > 0) {
                ARROW_SCROLL_UP.draw(left + LEFT_SIZE.getLeft() + ARROW_SCROLL_X, top + ARROW_SCROLL_UP_Y, (int) zLevel);
            }

            if (currentLoreText.size() - LORE_ROW_COUNT > textScrollIndex) {
                ARROW_SCROLL_DOWN.draw(left + LEFT_SIZE.getLeft() + ARROW_SCROLL_X, top + ARROW_SCROLL_DOWN_Y, (int) zLevel);
            }
        }

        // ARROWS - DIMENSION
        if (inBounds(left + TAB_BACK.getLeft(), top + TAB_BACK.getRight(), TAB_SIZE.getLeft(), TAB_SIZE.getRight(), x, y)) {
            ARROW_DIMENSION_BACK_MOUSEOVER.draw(left + ARROW_DIMENSION_BACK_POS.getLeft(), top + ARROW_DIMENSION_BACK_POS.getRight(), (int) zLevel);
        } else {
            ARROW_DIMENSION_BACK.draw(left + ARROW_DIMENSION_BACK_POS.getLeft(), top + ARROW_DIMENSION_BACK_POS.getRight(), (int) zLevel);
        }

        if (inBounds(left + TAB_FORWARD.getLeft(), top + TAB_FORWARD.getRight(), TAB_SIZE.getLeft(), TAB_SIZE.getRight(), x, y)) {
            ARROW_DIMENSION_FORWARD_MOUSEOVER.draw(left + ARROW_DIMENSION_FORWARD_POS.getLeft(), top + ARROW_DIMENSION_FORWARD_POS.getRight(), (int) zLevel);
        } else {
            ARROW_DIMENSION_FORWARD.draw(left + ARROW_DIMENSION_FORWARD_POS.getLeft(), top + ARROW_DIMENSION_FORWARD_POS.getRight(), (int) zLevel);
        }

        // ARROWS - PAGE
//		if (inBounds(left + ARROW_PAGE_BACK_POS.getLeft(), top + ARROW_PAGE_BACK_POS.getRight(), ARROW_PAGE_SIZE.getLeft(), ARROW_PAGE_SIZE.getRight(), x, y)) {
//			ARROW_PAGE_BACK_MOUSEOVER.draw(left + ARROW_PAGE_BACK_POS.getLeft(), top + ARROW_PAGE_BACK_POS.getRight(), (int)zLevel);
//		} else {
//			ARROW_PAGE_BACK.draw(left + ARROW_PAGE_BACK_POS.getLeft(), top + ARROW_PAGE_BACK_POS.getRight(), (int)zLevel);
//		}

//		if (inBounds(left + ARROW_PAGE_FORWARD_POS.getLeft(), top + ARROW_PAGE_FORWARD_POS.getRight(), ARROW_PAGE_SIZE.getLeft(), ARROW_PAGE_SIZE.getRight(), x, y)) {
//			ARROW_PAGE_FORWARD_MOUSEOVER.draw(left + ARROW_PAGE_FORWARD_POS.getLeft(), top + ARROW_PAGE_FORWARD_POS.getRight(), (int)zLevel);
//		} else {
//			ARROW_PAGE_FORWARD.draw(left + ARROW_PAGE_FORWARD_POS.getLeft(), top + ARROW_PAGE_FORWARD_POS.getRight(), (int)zLevel);
//		}

        // TEXT - LEFT
        String pretty = prettyCatCache.get(currentCategory);
        if (pretty == null) {
            pretty = StringHelper.pretty(currentCategory);
            prettyCatCache.put(currentCategory, pretty);
        }

        drawCenteredString(pretty, (left + (LEFT_SIZE.getLeft() - 8) / 2) + 8, top + TITLE_Y, 0x000000);

        // TEXT - RIGHT
        if (current != null) {
            drawCenteredString(current.getContent().getTitle(), left + LEFT_SIZE.getLeft() + (RIGHT_SIZE.getLeft() / 2), top + TITLE_Y, 0x000000);
            for (int i = textScrollIndex; i < Math.min(textScrollIndex + LORE_ROW_COUNT, currentLoreText.size()); i++) {
                String lore = currentLoreText.get(i);
                drawString(lore, left + LEFT_SIZE.getLeft() + BODY_X, (top + BODY_y + ClientProxy.fontRendererSmall.FONT_HEIGHT) + ClientProxy.fontRendererSmall.FONT_HEIGHT * (i - textScrollIndex), TEXT_SCALE, 0x000000, true);
            }
        }

        // TOOLTIPS - They affect lighting, so happen at the end
        index = 1;
        for (Lore lore : all) {
            if (lore != null && !lore.isHidden()) {
                if (!playerLore.contains(lore.getKey())) {
                    index++;
                    continue;
                }

                mc.getTextureManager().bindTexture(JOURNAL_RIGHT);

                int page = Math.min(index, 35);

                index++;

                if (page > 0 && page < 35) {
                    int drawX = (((page - 1) % 5) * LORE_BOX_GAP);
                    int drawY = 0;
                    if ((page - 1) > 4)
                        drawY = (((page - 1) / 5) * LORE_BOX_GAP);

                    // TOOLTIPS
                    if (inBounds(left + BOX_START.getLeft() + drawX, top + BOX_START.getRight() + drawY, 17, 17, x, y))
                        drawHoveringText(Collections.singletonList(lore.getContent().getTitle()), x, y, mc.fontRendererObj);
                }
            }
        }

        // ARROWS - DIMENSION
        if (inBounds(left + TAB_BACK.getLeft(), top + TAB_BACK.getRight(), TAB_SIZE.getLeft(), TAB_SIZE.getRight(), x, y))
            drawHoveringText(Collections.singletonList(I18n.format("gui.loreexpansion.previous")), x, y, mc.fontRendererObj);

        if (inBounds(left + TAB_FORWARD.getLeft(), top + TAB_FORWARD.getRight(), TAB_SIZE.getLeft(), TAB_SIZE.getRight(), x, y))
            drawHoveringText(Collections.singletonList(I18n.format("gui.loreexpansion.previous")), x, y, mc.fontRendererObj);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (button != 0)
            return;

        int left = (width - TOTAL_SIZE.getLeft()) / 2;
        int top = (height - TOTAL_SIZE.getRight()) / 2;

        Lore current = null;
        if (selectedLore != null) {
            current = LoreLoader.getLore(selectedLore);

            if (current == null)
                selectedLore = null;
        }

        Set<Lore> all = LoreLoader.getLoreForCategory(currentCategory);

        int index = 1;
        for (Lore lore : all) {
            if (lore != null && !lore.isHidden()) {
                if (!playerLore.contains(lore.getKey())) {
                    index++;
                    continue;
                }

                int page = Math.min(index, 35);

                index++;

                if (page > 0 && page < 35) {
                    int drawX = (((page - 1) % 5) * LORE_BOX_GAP);
                    int drawY = 0;
                    if ((page - 1) > 4) {
                        drawY = (((page - 1) / 5) * LORE_BOX_GAP);
                    }

                    if (inBounds(left + BOX_START.getLeft() + drawX, top + BOX_START.getRight() + drawY, 17, 17, x, y)) {
                        selectedLore = lore.getKey();
                        changeLore(selectedLore);
                    }
                }
            }
        }

        // AUDIO CONTROL
        if (current != null) {
            String sound = current.getContent().getAudio();
            if (!Strings.isNullOrEmpty(sound)) {
                if (LESoundHandler.INSTANCE.isPlaying(sound)) {
                    if (inBounds(left + LEFT_SIZE.getLeft() + STOP_POS.getLeft(), top + STOP_POS.getRight(), 5, 5, x, y)) {
                        LESoundHandler.INSTANCE.stop();
                    } else if (inBounds(left + LEFT_SIZE.getLeft() + PLAY_POS.getLeft(), top + PLAY_POS.getRight(), 4, 7, x, y)) {
                        if (!LESoundHandler.INSTANCE.isPaused()) {
                            LESoundHandler.INSTANCE.pause();
                        } else {
                            LESoundHandler.INSTANCE.resume();
                        }
                    }
                } else if (inBounds(left + LEFT_SIZE.getLeft() + PLAY_POS.getLeft(), top + PLAY_POS.getRight(), 4, 7, x, y)) {
                    LESoundHandler.INSTANCE.play(sound);
                }
            }
        }

        // ARROWS - SCROLL
        if (current != null) {
            if (inBounds(left + LEFT_SIZE.getLeft() + ARROW_SCROLL_X, top + ARROW_SCROLL_UP_Y, 13, 16, x, y)) {
                scrollText(-1);
            }

            if (inBounds(left + LEFT_SIZE.getLeft() + ARROW_SCROLL_X, top + ARROW_SCROLL_DOWN_Y, 13, 16, x, y)) {
                scrollText(1);
            }
        }

        // ARROWS - DIMENSION
        if (inBounds(left + TAB_BACK.getLeft(), top + TAB_BACK.getRight(), TAB_SIZE.getLeft(), TAB_SIZE.getRight(), x, y)) {
            changeCategory(categoryIndex - 1);
        }

        if (inBounds(left + TAB_FORWARD.getLeft(), top + TAB_FORWARD.getRight(), TAB_SIZE.getLeft(), TAB_SIZE.getRight(), x, y)) {
            changeCategory(categoryIndex + 1);
        }
    }

    public void reset() {
        maxPage = LoreLoader.getLoreForCategory(currentCategory).size();
        selectedLore = null;
        textScrollIndex = 0;
        currentLoreText.clear();
        LESoundHandler.INSTANCE.stop();
    }

    public void changeCategory(int index) {
        final int max = LoreLoader.getCategories().size() - 1;

        categoryIndex = index;
        if (categoryIndex < 0) categoryIndex = max;
        if (categoryIndex > max) categoryIndex = 0;

        String cat = LoreLoader.getCategories().get(categoryIndex);
        if (currentCategory == null || !currentCategory.equals(cat)) {
            currentCategory = cat;
            reset();
        }
    }

    public void changeCategory(String category) {
        currentCategory = category;
        categoryIndex = LoreLoader.getCategories().indexOf(currentCategory);

        reset();
    }

    public void changeLore(LoreKey key) {
        currentLore = LoreLoader.getLore(key);
        if (currentLore == null)
            return;

        currentLoreText.clear();
        selectedLore = key;

        String[] lore = currentLore.getContent().getBody().split("[\r\n]");
        List<String> newList = new ArrayList<String>();

        for (int i = 0; i < lore.length; i++) {
            String str = lore[i];

            if (!str.isEmpty()) {
                str = str.trim();
                str = str.replace("\t", "");
                str = StringHelper.indent(INDENTATION) + str;
                newList.add(str);
                if (i != lore.length - 1) {
                    newList.add("");
                }
            }
        }

        for (String str : newList) {
            currentLoreText.addAll(ClientProxy.fontRendererSmall.listFormattedStringToWidth(str, (int) (((RIGHT_SIZE.getLeft()) - 45) / TEXT_SCALE)));
        }
    }

    public void onWheelScrolled(int x, int y, int wheel) {
        int left = (width - TOTAL_SIZE.getLeft()) / 2;
        int top = (height - TOTAL_SIZE.getRight()) / 2;

        if (inBounds(left, top, LEFT_SIZE.getLeft(), LEFT_SIZE.getRight(), x, y)) {
//			scrollLore(-wheel);
        } else if (inBounds(left + LEFT_SIZE.getLeft(), top, RIGHT_SIZE.getLeft(), RIGHT_SIZE.getRight(), x, y)) {
            scrollText(-wheel);
        }
    }

    @Override
    protected void keyTyped(char key, int code) throws IOException {
        super.keyTyped(key, code);

        if (code == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }

        if (code == Keyboard.KEY_UP) {
            scrollText(-1);
        }

        if (code == Keyboard.KEY_DOWN) {
            scrollText(1);
        }
    }

    public void scrollLore(int theta) {
        if (theta < 0) {
            loreScrollIndex--;
            if (loreScrollIndex < 0) {
                loreScrollIndex = 0;
            }
        }

        if (theta > 0) {
            loreScrollIndex++;
            if (loreScrollIndex > Math.max(0, Math.ceil(maxPage / 5))) {
                loreScrollIndex = (int) Math.max(0, Math.ceil(maxPage / 5));
            }
        }
    }

    public void scrollText(int theta) {
        if (theta < 0) {
            textScrollIndex -= 2;
            if (textScrollIndex < 0) {
                textScrollIndex = 0;
            }
        }

        if (theta > 0) {
            textScrollIndex += 2;
            if (textScrollIndex > Math.max(0, currentLoreText.size() - LORE_ROW_COUNT)) {
                textScrollIndex = Math.max(0, currentLoreText.size() - LORE_ROW_COUNT);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean inBounds(int x, int y, int w, int h, int mX, int mY) {
        return (x <= mX) && (mX <= x + w) && (y <= mY) && (mY <= y + h);
    }

    public void drawCenteredString(String str, int x, int y, int color) {
        drawString(str, x - mc.fontRendererObj.getStringWidth(str) / 2, y, color);
    }

    public void drawString(String str, int x, int y, int color) {
        drawString(str, x, y, 1.0F, color, false);
    }

    public void drawString(String str, int x, int y, float mult, int color, boolean custom) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(mult, mult, 1.0F);
        if (custom) {
            ClientProxy.fontRendererSmall.drawString(str, (int) ((x) / mult), (int) ((y) / mult), color);
        } else {
            mc.fontRendererObj.drawString(str, (int) ((x) / mult), (int) ((y) / mult), color);
        }
        GlStateManager.popMatrix();
    }
}