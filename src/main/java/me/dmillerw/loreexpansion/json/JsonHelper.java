package me.dmillerw.loreexpansion.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();
}
