package me.dmillerw.loreexpansion.extensions.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import kotlin.reflect.KClass

/**
 * @author dmillerw
 */

public fun <T : Any> Gson.fromFile(json: File, clazz: KClass<T>): T {
    return this.fromJson(FileReader(json), clazz.java);
}

public fun <T : Any> Gson.fromString(json: String, clazz: KClass<T>): T {
    return this.fromJson(json, clazz.java)
}

public fun <T : Any> GsonBuilder.registerAdapter(clazz: KClass<T>, adapter: Any) {
    this.registerTypeAdapter(clazz.java, adapter);
}