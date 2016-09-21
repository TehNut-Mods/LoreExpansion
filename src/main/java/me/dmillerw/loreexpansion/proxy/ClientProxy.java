package me.dmillerw.loreexpansion.proxy;

import me.dmillerw.loreexpansion.LoreConfiguration;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.client.sound.LESoundHandler;
import me.dmillerw.loreexpansion.client.texture.SmallFontRenderer;
import me.dmillerw.loreexpansion.core.data.LoreKey;
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

import java.io.File;
import java.lang.reflect.Field;
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

        if (!LoreConfiguration.theme.equalsIgnoreCase("default")) {
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
}
