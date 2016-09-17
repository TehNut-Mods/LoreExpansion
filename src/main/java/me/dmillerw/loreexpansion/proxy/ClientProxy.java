package me.dmillerw.loreexpansion.proxy;

import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

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
    }
}
