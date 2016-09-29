package me.dmillerw.loreexpansion.core.action;

import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.SerializerBase;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public interface ILoreAction<T> {

    void act(EntityPlayer player, Lore lore);

    @Nonnull
    SerializerBase<T> getSerializer();

    @Nonnull
    Type getType();
}
