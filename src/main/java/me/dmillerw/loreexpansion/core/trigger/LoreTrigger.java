package me.dmillerw.loreexpansion.core.trigger;

import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.SerializerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface LoreTrigger<T> {

    boolean trigger(World world, EntityPlayer player, Lore lore, T triggerTarget);

    SerializerBase<T> getSerializer();

    Class<T> getType();
}
