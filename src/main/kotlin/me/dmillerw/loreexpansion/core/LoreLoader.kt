package me.dmillerw.loreexpansion.core

import me.dmillerw.loreexpansion.extensions.file.listFilesWithExtension
import me.dmillerw.loreexpansion.extensions.gson.fromFile
import me.dmillerw.loreexpansion.json.JsonHelper
import me.dmillerw.loreexpansion.json.data.Lore
import java.io.File

/**
 * @author dmillerw
 */
object LoreLoader {

    public fun initialize(directory: File) {
        for (file in directory.listFilesWithExtension("json")) {
            val lore = JsonHelper.gson.fromFile(file, Lore::class)
            println(lore)
        }
    }
}