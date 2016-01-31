package me.dmillerw.loreexpansion.proxy

import me.dmillerw.loreexpansion.LoreExpansion
import me.dmillerw.loreexpansion.core.LoreLoader
import me.dmillerw.loreexpansion.core.player.PlayerEventHandler
import me.dmillerw.loreexpansion.item.ItemLore
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

/**
 * @author dmillerw
 */
open class CommonProxy {

    public open fun preInit(event: FMLPreInitializationEvent) {
        ItemLore.register()

        LoreLoader.initialize(LoreExpansion.loreFolder)

        MinecraftForge.EVENT_BUS.register(PlayerEventHandler)
    }

    public open fun init(event: FMLInitializationEvent) {

    }

    public open fun postInit(event: FMLPostInitializationEvent) {

    }
}