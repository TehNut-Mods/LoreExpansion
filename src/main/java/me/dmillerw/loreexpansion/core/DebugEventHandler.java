package me.dmillerw.loreexpansion.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

public class DebugEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void dumpAtlas(ArrowLooseEvent evt) {
        if (!(Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
            return;

        if (!evt.getEntityPlayer().worldObj.isRemote || !evt.getEntityPlayer().isSneaking())
            return;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

        FMLLog.fine("Dumped atlas {} wide by {} tall", width, height);

        int pixels = width * height;

        IntBuffer buffer = BufferUtils.createIntBuffer(pixels);
        int[] pixelValues = new int[pixels];

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);

        buffer.get(pixelValues);

        BufferedImage bufferedimage = new BufferedImage(width, height, 2);

        for (int yPixel = 0; yPixel < height; ++yPixel)
            for (int xPixel = 0; xPixel < width; ++xPixel)
                bufferedimage.setRGB(xPixel, yPixel, pixelValues[yPixel * width + xPixel]);

        File mcFolder = Minecraft.getMinecraft().mcDataDir;
        File result = new File(mcFolder, "atlas.png");

        try {
            ImageIO.write(bufferedimage, "png", result);
        } catch (IOException e) {
            FMLLog.fine("Failed to dump debug atlas");
        }
    }
}
