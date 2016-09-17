package me.dmillerw.loreexpansion.proxy;

import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.client.texture.SmallFontRenderer;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static LoreKey pickedUpPage;

    public static SmallFontRenderer fontRendererSmall;

    @Override
    public void preInit() {
        super.preInit();

        ModelLoader.setCustomModelResourceLocation(LoreExpansion.LORE_JOURNAL, 0, new ModelResourceLocation(LoreExpansion.LORE_JOURNAL.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(LoreExpansion.LORE_PAGE, 0, new ModelResourceLocation(LoreExpansion.LORE_PAGE.getRegistryName(), "inventory"));
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();

        fontRendererSmall = new SmallFontRenderer(Minecraft.getMinecraft().gameSettings,  new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
    }
}
