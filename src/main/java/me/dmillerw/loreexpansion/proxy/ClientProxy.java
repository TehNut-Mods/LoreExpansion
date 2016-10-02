package me.dmillerw.loreexpansion.proxy;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import me.dmillerw.loreexpansion.LoreConfiguration;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.client.KeyHandler;
import me.dmillerw.loreexpansion.client.sound.LESoundHandler;
import me.dmillerw.loreexpansion.client.texture.SmallFontRenderer;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.json.Serializers;
import me.dmillerw.loreexpansion.util.GeneralUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static LoreKey pickedUpPage;
    public static SmallFontRenderer fontRendererSmall;

    @Override
    public void preInit() {
        super.preInit();

        setModel(LoreExpansion.LORE_JOURNAL, 0, LoreExpansion.LORE_JOURNAL.getRegistryName());
        setModel(LoreExpansion.LORE_JOURNAL, 1, new ResourceLocation(LoreExpansion.ID, LoreExpansion.LORE_JOURNAL.getRegistryName().getResourcePath() + "_creative"));
        setModel(LoreExpansion.LORE_PAGE, 0, LoreExpansion.LORE_PAGE.getRegistryName());

        MinecraftForge.EVENT_BUS.register(LESoundHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new KeyHandler());

        if (!LoreConfiguration.theme.equalsIgnoreCase("default")) {

            if (!LoreExpansion.themeDir.exists())
                LoreExpansion.themeDir.mkdirs();

            try {
                URL themesURL = ClientProxy.class.getResource(resLocToResPath(new ResourceLocation(LoreExpansion.ID, "default_themes.json")));
                List<String> defaultThemes = Serializers.getGson().fromJson(Resources.toString(themesURL, Charsets.UTF_8), new TypeToken<List<String>>(){}.getType());
                for (String defaultTheme : defaultThemes) {
                    URL themeURL = ClientProxy.class.getResource(resLocToResPath(new ResourceLocation(LoreExpansion.ID, defaultTheme)));
                    File movedFile = new File(LoreExpansion.themeDir, defaultTheme);
                    if (!new File(FilenameUtils.getBaseName(defaultTheme)).exists()) { // Don't generate theme files if a folder of the same name exists
                        FileUtils.copyURLToFile(themeURL, movedFile);
                        GeneralUtil.extractZip(movedFile);
                    }
                }
            } catch (Exception e) {
                LoreExpansion.LOGGER.error("Error extracting default themes.");
                e.printStackTrace();
            }

            File themeFolder = new File(LoreExpansion.themeDir, LoreConfiguration.theme);
            if (themeFolder.exists()) {
                FolderResourcePack resourcePack = new FolderResourcePack(themeFolder);
                Field _defaultResourcePacks = ReflectionHelper.findField(Minecraft.class, "defaultResourcePacks", "field_110449_ao");
                try {
                    _defaultResourcePacks.setAccessible(true);
                    //noinspection unchecked
                    List<IResourcePack> resourcePacks = (List<IResourcePack>) _defaultResourcePacks.get(Minecraft.getMinecraft());
                    resourcePacks.add(resourcePack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                LoreExpansion.LOGGER.error("Error loading theme pack {}. Directory {} does not exist. Falling back to default theme.", LoreConfiguration.theme, themeFolder);
            }
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();

        fontRendererSmall = new SmallFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
    }

    private void setModel(Item item, int meta, ResourceLocation location) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
    }

    private String resLocToResPath(ResourceLocation resourceLocation) {
        return "/assets/" + resourceLocation.getResourceDomain() + "/defaults/theme/" + resourceLocation.getResourcePath();
    }
}
