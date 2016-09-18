package me.dmillerw.loreexpansion.event;

import me.dmillerw.loreexpansion.core.data.Lore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is fired whenever a player obtains a piece of lore and is only fired on the server side.
 *
 * This even is cancellable. Cancelling this event will stop the player from obtaining the lore.
 *
 * {@link #player} - The player obtaining the lore.
 * {@link #lore} - The lore being obtained.
 */
@Cancelable
public class LoreObtainedEvent extends Event {

    private final EntityPlayerMP player;
    private final Lore lore;

    public LoreObtainedEvent(EntityPlayerMP player, Lore lore) {
        this.player = player;
        this.lore = lore;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }

    public Lore getLore() {
        return lore;
    }
}
