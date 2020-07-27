package me.dmillerw.loreexpansion.core.loader;

import me.dmillerw.loreexpansion.core.data.Lore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

public interface ILoreLoader {

    void gatherPages(Consumer<Lore> pages);

}
