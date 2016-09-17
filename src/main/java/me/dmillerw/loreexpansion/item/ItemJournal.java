package me.dmillerw.loreexpansion.item;

import me.dmillerw.loreexpansion.LoreExpansion;
import net.minecraft.item.Item;

public class ItemJournal extends Item {

    public ItemJournal() {
        setUnlocalizedName(LoreExpansion.ID + ".journal");
        setCreativeTab(LoreExpansion.TAB_LORE);
        setMaxStackSize(1);
    }
}
