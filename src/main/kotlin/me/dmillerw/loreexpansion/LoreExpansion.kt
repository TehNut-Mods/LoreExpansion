package me.dmillerw.loreexpansion

import me.dmillerw.loreexpansion.extensions.file.makeDirectory
import me.dmillerw.loreexpansion.proxy.CommonProxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File

/**
 * @author dmillerw
 */

@Mod(modid = LoreExpansion.ID, name = LoreExpansion.NAME, version = LoreExpansion.VERSION, modLanguageAdapter = "me.dmillerw.loreexpansion.adapter.KotlinAdapter")
public object LoreExpansion {

    const val ID = "LoreExpansion-Kotlin";
    const val NAME = "Lore Expansion";
    const val VERSION = "%MOD_VERSION%";

    const val PACKAGE = "me.dmillerw.loreexpansion."

    @SidedProxy(serverSide = PACKAGE + "proxy.CommonProxy", clientSide = PACKAGE + "proxy.ClientProxy")
    lateinit var proxy: CommonProxy;

    lateinit var loreFolder: File;

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        loreFolder = File(event.modConfigurationDirectory, "LoreExpansion").makeDirectory()
        proxy.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit(event)
    }
}