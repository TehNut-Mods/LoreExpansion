package me.dmillerw.loreexpansion.client.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SubTexture {

    private final ResourceLocation texture;
    private final int textureX;
    private final int textureY;
    private final int textureWidth;
    private final int textureHeight;

    public SubTexture(ResourceLocation texture, int textureX, int textureY, int textureWidth, int textureHeight) {
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public void draw(int xPos, int yPos, double zLevel) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        float f = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double) xPos, (double) (yPos + textureHeight), zLevel).tex((double) ((float) (textureX) * f), (double) ((float) (textureY + textureHeight) * f)).endVertex();
        vertexbuffer.pos((double) (xPos + textureWidth), (double) (yPos + textureHeight), zLevel).tex((double) ((float) (textureX + textureWidth) * f), (double) ((float) (textureY + textureHeight) * f)).endVertex();
        vertexbuffer.pos((double) (xPos + textureWidth), (double) (yPos), zLevel).tex((double) ((float) (textureX + textureWidth) * f), (double) ((float) (textureY) * f)).endVertex();
        vertexbuffer.pos((double) xPos, (double) (yPos), zLevel).tex((double) ((float) (textureX) * f), (double) ((float) (textureY) * f)).endVertex();
        tessellator.draw();
    }
}
