package me.dmillerw.loreexpansion.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.dmillerw.loreexpansion.extensions.gson.registerAdapter
import me.dmillerw.loreexpansion.json.adapter.deserializer.ReferenceDeserializer
import me.dmillerw.loreexpansion.json.data.Reference

/**
 * @author dmillerw
 */

object JsonHelper {

    val gson: Gson by lazy {
        val builder = GsonBuilder()
        builder.setPrettyPrinting()

        builder.registerAdapter(Reference::class, ReferenceDeserializer())

        builder.create()
    }
}