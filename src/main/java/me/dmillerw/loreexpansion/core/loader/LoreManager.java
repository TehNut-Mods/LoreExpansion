package me.dmillerw.loreexpansion.core.loader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreCachedData;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public class LoreManager {

    private static final Multimap<String, ILoreLoader> LOADERS = ArrayListMultimap.create();
    public static final ArrayListMultimap<String, LoreCachedData> LORES = ArrayListMultimap.create();

    public static void gatherLoaders(ASMDataTable dataTable) {
        Set<ASMDataTable.ASMData> discoveredLoaders = dataTable.getAll(LoreLoader.class.getName());

        for (ASMDataTable.ASMData data : discoveredLoaders) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                ILoreLoader loaderInstance = null;
                if (!data.getObjectName().equals(data.getClassName())) { // Load fields
                    Field potentialLoader = asmClass.getDeclaredField(data.getObjectName());
                    if (!Modifier.isStatic(potentialLoader.getModifiers())) {
                        LoreExpansion.LOGGER.error("Field at {}.{} was annotated with @ILoreLoader.Gather but is not static.", data.getClassName(), data.getObjectName());
                        continue;
                    }

                    if (potentialLoader.getType() != ILoreLoader.class) {
                        LoreExpansion.LOGGER.error("Field at {}.{} was annotated with @ILoreLoader.Gather but is not an ILoreLoader.", data.getClassName(), data.getObjectName());
                        continue;
                    }

                    loaderInstance = (ILoreLoader) potentialLoader.get(null);
                } else if (ILoreLoader.class.isAssignableFrom(asmClass)) {
                    // noinspection unchecked
                    Class<ILoreLoader> loaderClass = (Class<ILoreLoader>) asmClass;
                    loaderInstance = loaderClass.newInstance();
                }

                if (loaderInstance == null)
                    continue;

                LoreExpansion.LOGGER.debug("Discovered a Lore loader at {}.{}", data.getClassName(), data.getObjectName());
                LOADERS.put((String) data.getAnnotationInfo().get("value"), loaderInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadLore() {
        LORES.clear();

        LOADERS.forEach((k, v) -> {
            List<Lore> lores = Lists.newArrayList();
            v.gatherPages(lores::add);
            LORES.put(k, new LoreCachedData(lores));
        });
    }

    public static void initLoreDir() {
        if (!LoreExpansion.loreDir.exists() && LoreExpansion.loreDir.mkdirs()) {
            try {
                FileUtils.copyURLToFile(LoreManager.class.getResource("/assets/loreexpansion/defaults/lore/tutorial.json"), new File(LoreExpansion.loreDir, "tutorial.json"));;
                FileUtils.copyURLToFile(LoreManager.class.getResource("/assets/loreexpansion/defaults/lore/tutorial.ogg"), new File(LoreExpansion.audioDir, "tutorial.ogg"));;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static LoreCachedData getLoreData(String mod, int index) {
        return LORES.get(mod).get(index);
    }
}
