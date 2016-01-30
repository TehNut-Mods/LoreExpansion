package me.dmillerw.loreexpansion.test

import me.dmillerw.loreexpansion.extensions.gson.fromString
import me.dmillerw.loreexpansion.json.JsonHelper
import me.dmillerw.loreexpansion.json.data.Lore

/**
 * @author dmillerw
 */

fun main(args: Array<String>) {
    val json = """
        {
            "id": "FILE_1",
            "category": "CAT_1",
            "content": {
                "title": "title",
                "body": "text",
                "audio": "audio"
            },
            "requirements": [
                "file2;cat2",
                ["file3", "cat2"]
            ]
        }
        """

    val lore = JsonHelper.gson.fromString(json, Lore::class)
    print(lore)
}