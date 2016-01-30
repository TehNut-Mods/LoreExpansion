package me.dmillerw.loreexpansion.proxy

import me.dmillerw.loreexpansion.LoreExpansion
import me.dmillerw.loreexpansion.core.LoreLoader
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

/**
 * @author dmillerw
 */
open class CommonProxy {

    public open fun preInit(event: FMLPreInitializationEvent) {
        LoreLoader.initialize(LoreExpansion.loreFolder)
    }

    public open fun init(event: FMLInitializationEvent) {

    }

    public open fun postInit(event: FMLPostInitializationEvent) {

    }
}