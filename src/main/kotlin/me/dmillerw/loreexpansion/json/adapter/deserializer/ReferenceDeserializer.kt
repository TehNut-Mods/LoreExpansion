package me.dmillerw.loreexpansion.json.adapter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import me.dmillerw.loreexpansion.json.data.Reference
import java.lang.reflect.Type

/**
 * @author dmillerw
 */
class ReferenceDeserializer : JsonDeserializer<Reference> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Reference? {
        if (json == null)
            throw JsonParseException("Cannot parse from null element!")

        var id = "";
        var category = "";

        if (json.isJsonObject) {
            val jobject = json.asJsonObject;
            id = jobject.getAsJsonPrimitive("id").asString;
            category = jobject.getAsJsonPrimitive("category").asString;
        } else if (json.isJsonArray) {
            val array = json.asJsonArray;
            id = array[0].asString;
            category = array[1].asString;
        } else if (json.isJsonPrimitive) {
            val primitive = json.asJsonPrimitive;
            if (primitive.isString) {
                val string = primitive.asString.split(';');
                id = string[0];
                category = string[1];
            }
        }

        return Reference(id, category);
    }
}