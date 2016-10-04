package me.dmillerw.loreexpansion.core;

import com.google.common.collect.*;
import com.google.gson.Gson;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.core.json.Serializers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoreLoader {

    public static final Set<Lore> LOADED_LORE = Sets.newHashSet();
    public static final Gson GSON = Serializers.getStdGson();

    private static List<String> categories = Lists.newArrayList();
    private static Map<LoreKey, Lore> lore = Maps.newHashMap();
    private static ArrayListMultimap<String, Lore> sortedLore = ArrayListMultimap.create();

    public static void init(File loreDir, boolean initialRun) {
        if (initialRun && !loreDir.exists() && loreDir.mkdirs()) {
            try {
                FileUtils.copyURLToFile(LoreLoader.class.getResource("/assets/loreexpansion/defaults/lore/tutorial.ogg"), new File(LoreExpansion.audioDir, "tutorial.ogg"));
                FileUtils.copyURLToFile(LoreLoader.class.getResource("/assets/loreexpansion/defaults/lore/tutorial.json"), new File(LoreExpansion.loreDir, "tutorial.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!initialRun) {
            LOADED_LORE.clear();
            categories.clear();
            lore.clear();
            sortedLore.clear();
        }

        Set<LoreKey> keys = Sets.newHashSet();
        File[] jsonFiles = loreDir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (jsonFiles == null)
            return;

        try {
            for (File file : jsonFiles) {
                Lore lore = GSON.fromJson(new InputStreamReader(new FileInputStream(file), "UTF-8"), Lore.class);
                if (keys.contains(lore.getKey())) {
                    LoreExpansion.LOGGER.error("Duplicate Lore id: {}", lore.getKey().getId());
                } else {
                    LOADED_LORE.add(lore);
                    keys.add(lore.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Lore loadedLore : LOADED_LORE) {
            lore.put(loadedLore.getKey(), loadedLore);
            if (!categories.contains(loadedLore.getKey().getCategory()))
                categories.add(loadedLore.getKey().getCategory());
        }

        for (String category : categories) {
            for (Lore lore : LOADED_LORE)
                if (lore.getKey().getCategory().equalsIgnoreCase(category))
                    sortedLore.put(category, lore);

            Collections.sort(sortedLore.get(category));
        }
    }

    public static Lore getLore(LoreKey key) {
        return lore.get(key);
    }

    public static List<Lore> getLoreForCategory(String category) {
        return sortedLore.get(category) == null ? Collections.<Lore>emptyList() : sortedLore.get(category);
    }

    public static List<String> getCategories() {
        return ImmutableList.copyOf(categories);
    }
}
