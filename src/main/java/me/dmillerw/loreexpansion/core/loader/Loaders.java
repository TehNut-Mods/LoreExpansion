package me.dmillerw.loreexpansion.core.loader;

import com.google.common.collect.Sets;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.json.Serializers;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Set;

public class Loaders {

    @LoreLoader(LoreExpansion.ID)
    public static final ILoreLoader JSON_LOADER = pages -> {
        if (!LoreExpansion.loreDir.exists())
            return;

        File[] jsonFiles = LoreExpansion.loreDir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (jsonFiles == null)
            return;

        Set<Lore> discovered = Sets.newHashSet();
        for (File file : jsonFiles) {
            try (FileReader reader = new FileReader(file)) {
                Lore lore = Serializers.getStdGson().fromJson(reader, Lore.class);
                if (!discovered.contains(lore)) {
                    pages.accept(lore);
                    discovered.add(lore);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
