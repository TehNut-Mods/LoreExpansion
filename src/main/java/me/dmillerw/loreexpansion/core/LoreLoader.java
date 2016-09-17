package me.dmillerw.loreexpansion.core;

import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class LoreLoader {

    public static final Set<Lore> LOADED_LORE = Sets.newHashSet();
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public static List<String> categories = Lists.newArrayList();
    private static Map<String, Lore> lore = Maps.newHashMap();
    private static Map<String, Set<Lore>> sortedLore = Maps.newHashMap();

    public static void init(File loreDir) {
        if (!loreDir.exists() && loreDir.mkdirs()) {
            try {
                String json = GSON.toJson(Lore.NULL_LORE);
                FileWriter writer = new FileWriter(new File(loreDir, "null.json"));
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Set<String> names = Sets.newHashSet();
        File[] jsonFiles = loreDir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (jsonFiles == null)
            return;

        try {
            for (File file : jsonFiles) {
                Lore lore = GSON.fromJson(new FileReader(file), Lore.class);
                if (names.contains(lore.getKey().getId())) {
                    LoreExpansion.LOGGER.error("Duplicate Lore id: {}", lore.getKey().getId());
                } else {
                    LOADED_LORE.add(lore);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImmutableMap.Builder<String, Lore> loreBuilder = ImmutableMap.builder();
        ImmutableList.Builder<String> categoryBuilder = ImmutableList.builder();

        for (Lore lore : LOADED_LORE) {
            loreBuilder.put(lore.getKey().getId(), lore);
            categoryBuilder.add(lore.getKey().getCategory());
        }

        lore = loreBuilder.build();
        categories = categoryBuilder.build();

        ImmutableMap.Builder<String, Set<Lore>> sortedBuilder = ImmutableMap.builder();
        for (String category : categories) {
            Set<Lore> lores = Sets.newHashSet();
            for (Lore lore : LOADED_LORE)
                if (lore.getKey().getCategory().equalsIgnoreCase(category))
                    lores.add(lore);

            sortedBuilder.put(category, lores);
        }
        sortedLore = sortedBuilder.build();
    }

    public static Lore getLore(String key) {
        return lore.get(key);
    }

    public static Lore getLore(LoreKey key) {
        return lore.get(key.getId());
    }

    public static Set<Lore> getLoreForCategory(String category) {
        if (category.equalsIgnoreCase("global"))
            return LOADED_LORE;
        return sortedLore.get(category) == null ? Collections.<Lore>emptySet() : sortedLore.get(category);
    }
}
