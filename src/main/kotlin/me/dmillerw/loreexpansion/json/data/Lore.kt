package me.dmillerw.loreexpansion.json.data

/**
 * @author dmillerw
 */
public data class Lore(
        val id: String,
        val category: String,
        val content: Content,
        val requirements: Set<Reference>)