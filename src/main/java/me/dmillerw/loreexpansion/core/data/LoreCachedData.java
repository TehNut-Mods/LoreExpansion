package me.dmillerw.loreexpansion.core.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class LoreCachedData {

    private final List<String> categories;
    private final Map<LoreKey, Lore> lore;
    private final ArrayListMultimap<String, Lore> sortedLore;

    public LoreCachedData(List<Lore> lores) {
        this.categories = Lists.newArrayList();
        this.lore = Maps.newHashMap();
        this.sortedLore = ArrayListMultimap.create();

        init(lores);
    }

    private void init(List<Lore> lores) {
        for (Lore l : lores) {
            lore.put(l.getKey(), l);
            if (!categories.contains(l.getKey().getCategory()))
                categories.add(l.getKey().getCategory());
        }

        for (String category : categories) {
            for (Lore l : lore.values())
                if (category.equals(l.getKey().getCategory()))
                    sortedLore.put(category, l);

            sortedLore.get(category).sort(Lore::compareTo);
        }
    }

    public Lore getLore(LoreKey loreKey) {
        return lore.get(loreKey);
    }

    public List<Lore> getCateogry(String category) {
        List<Lore> sorted = sortedLore.get(category);
        return sorted == null ? Collections.emptyList() : sorted;
    }

    public List<String> getCategories() {
        return categories;
    }
}
