package me.dmillerw.loreexpansion.core

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import me.dmillerw.loreexpansion.extensions.file.listFilesWithExtension
import me.dmillerw.loreexpansion.extensions.gson.fromFile
import me.dmillerw.loreexpansion.json.JsonHelper
import me.dmillerw.loreexpansion.json.data.Lore
import me.dmillerw.loreexpansion.json.data.NULL_LORE
import me.dmillerw.loreexpansion.util.LogHelper
import java.io.File

/**
 * @author dmillerw
 */
object LoreLoader {

    private val EMPTY_SET = Sets.newHashSet<Lore>()

    val loadedLore = Sets.newHashSet<Lore>()

    lateinit var lore: Map<String, Lore>
    lateinit var categories: Set<String>
    lateinit var sortedLore: Map<String, Set<Lore>>

    public fun initialize(directory: File) {
        val names = Sets.newHashSet<String>()
        for (file in directory.listFilesWithExtension("json")) {
            val lore = JsonHelper.gson.fromFile(file, Lore::class)

            if (names.contains(lore.id)) {
                LogHelper.warn("Duplicate Lore id: ${lore.id}")
            } else {
                names.add(lore.id)
                loadedLore.add(lore)
            }
        }

        val loreBuilder = ImmutableMap.builder<String, Lore>()
        for (lore in loadedLore)
            loreBuilder.put(lore.id, lore)
        lore = loreBuilder.build()

        val categoryBuilder = ImmutableSet.builder<String>()
        for (lore in loadedLore)
            categoryBuilder.add(lore.category)
        categories = categoryBuilder.build()

        val sortedBuilder = ImmutableMap.builder<String, Set<Lore>>()
        for (category in categories) {
            val set = Sets.newHashSet<Lore>()
            for (lore in loadedLore) {
                if (lore.category.equals(category, true)) {
                    set.add(lore)
                }
            }
            sortedBuilder.put(category, set)
        }
        sortedLore = sortedBuilder.build()
    }

    public fun getLore(key: String) : Lore {
        return lore[key] ?: NULL_LORE
    }

    public fun getLoreForCategory(category: String) : Set<Lore> {
        return sortedLore[category] ?: EMPTY_SET
    }
}