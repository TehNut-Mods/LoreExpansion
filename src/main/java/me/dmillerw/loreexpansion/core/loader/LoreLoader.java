package me.dmillerw.loreexpansion.core.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface LoreLoader {

    /**
     * The mod ID of the mod who owns this lore set.
     *
     * @return the owning mod ID.
     */
    String value();
}
