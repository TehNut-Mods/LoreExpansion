package me.dmillerw.loreexpansion.json.data

import com.google.common.collect.Sets

/**
 * @author dmillerw
 */

public val NULL_LORE = Lore("NULL", "NULL", Content("NULL", "NULL", "NULL"), Sets.newHashSet())
public fun Lore.isNull() : Boolean {
    return id.equals("NULL") && category.equals("NULL")
}

public data class Lore(
        val id: String,
        val category: String,
        val content: Content,
        val requirements: Set<Reference>)