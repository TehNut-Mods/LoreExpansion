package me.dmillerw.loreexpansion.util

import me.dmillerw.loreexpansion.LoreExpansion
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.MarkerManager
import org.apache.logging.log4j.message.Message

object LogHelper {

    val MOD_MARKER = MarkerManager.getMarker(LoreExpansion.ID)
    private val logger = LogManager.getLogger(LoreExpansion.ID)

    fun log(level: Level, marker: Marker, message: Message) {
        logger.log(level, marker, message)
    }

    fun log(level: Level, marker: Marker, `object`: Any) {
        logger.log(level, marker, `object`)
    }

    fun log(level: Level, marker: Marker, message: String) {
        logger.log(level, marker, message)
    }

    fun log(level: Level, marker: Marker, format: String, vararg params: Any) {
        logger.log(level, marker, format, *params)
    }

    fun log(level: Level, message: Message) {
        log(level, MOD_MARKER, message)
    }

    fun log(level: Level, `object`: Any) {
        log(level, MOD_MARKER, `object`)
    }

    fun log(level: Level, message: String) {
        log(level, MOD_MARKER, message)
    }

    fun log(level: Level, format: String, vararg params: Any) {
        log(level, MOD_MARKER, format, *params)
    }

    // ALL

    fun all(marker: Marker, message: Message) {
        log(Level.ALL, marker, message)
    }

    fun all(marker: Marker, `object`: Any) {
        log(Level.ALL, marker, `object`)
    }

    fun all(marker: Marker, message: String) {
        log(Level.ALL, marker, message)
    }

    fun all(marker: Marker, format: String, vararg params: Any) {
        log(Level.ALL, marker, format, *params)
    }

    fun all(message: Message) {
        all(MOD_MARKER, message)
    }

    fun all(`object`: Any) {
        all(MOD_MARKER, `object`)
    }

    fun all(message: String) {
        all(MOD_MARKER, message)
    }

    fun all(format: String, vararg pararms: Any) {
        all(MOD_MARKER, format, *pararms)
    }

    // DEBUG

    fun debug(marker: Marker, message: Message) {
        log(Level.DEBUG, marker, message)
    }

    fun debug(marker: Marker, `object`: Any) {
        log(Level.DEBUG, marker, `object`)
    }

    fun debug(marker: Marker, message: String) {
        log(Level.DEBUG, marker, message)
    }

    fun debug(marker: Marker, format: String, vararg params: Any) {
        log(Level.DEBUG, marker, format, *params)
    }

    fun debug(message: Message) {
        debug(MOD_MARKER, message)
    }

    fun debug(`object`: Any) {
        debug(MOD_MARKER, `object`)
    }

    fun debug(message: String) {
        debug(MOD_MARKER, message)
    }

    fun debug(format: String, vararg params: Any) {
        debug(MOD_MARKER, format, *params)
    }

    // ERROR

    fun error(marker: Marker, message: Message) {
        log(Level.ERROR, marker, message)
    }

    fun error(marker: Marker, `object`: Any) {
        log(Level.ERROR, marker, `object`)
    }

    fun error(marker: Marker, message: String) {
        log(Level.ERROR, marker, message)
    }

    fun error(marker: Marker, format: String, vararg params: Any) {
        log(Level.ERROR, marker, format, *params)
    }

    fun error(message: Message) {
        error(MOD_MARKER, message)
    }

    fun error(`object`: Any) {
        error(MOD_MARKER, `object`)
    }

    fun error(message: String) {
        error(MOD_MARKER, message)
    }

    fun error(format: String, vararg params: Any) {
        error(MOD_MARKER, format, *params)
    }

    // FATAL

    fun fatal(marker: Marker, message: Message) {
        log(Level.FATAL, marker, message)
    }

    fun fatal(marker: Marker, `object`: Any) {
        log(Level.FATAL, marker, `object`)
    }

    fun fatal(marker: Marker, message: String) {
        log(Level.FATAL, marker, message)
    }

    fun fatal(marker: Marker, format: String, vararg params: Any) {
        log(Level.FATAL, marker, format, *params)
    }

    fun fatal(message: Message) {
        fatal(MOD_MARKER, message)
    }

    fun fatal(`object`: Any) {
        fatal(MOD_MARKER, `object`)
    }

    fun fatal(message: String) {
        fatal(MOD_MARKER, message)
    }

    fun fatal(format: String, vararg params: Any) {
        fatal(MOD_MARKER, format, *params)
    }

    // INFO

    fun info(marker: Marker, message: Message) {
        log(Level.INFO, marker, message)
    }

    fun info(marker: Marker, `object`: Any) {
        log(Level.INFO, marker, `object`)
    }

    fun info(marker: Marker, message: String) {
        log(Level.INFO, marker, message)
    }

    fun info(marker: Marker, format: String, vararg params: Any) {
        log(Level.INFO, marker, format, *params)
    }

    fun info(message: Message) {
        info(MOD_MARKER, message)
    }

    fun info(`object`: Any) {
        info(MOD_MARKER, `object`)
    }

    fun info(message: String) {
        info(MOD_MARKER, message)
    }

    fun info(format: String, vararg params: Any) {
        info(MOD_MARKER, format, *params)
    }

    // OFF

    fun off(marker: Marker, message: Message) {
        log(Level.OFF, marker, message)
    }

    fun off(marker: Marker, `object`: Any) {
        log(Level.OFF, marker, `object`)
    }

    fun off(marker: Marker, message: String) {
        log(Level.OFF, marker, message)
    }

    fun off(marker: Marker, format: String, vararg params: Any) {
        log(Level.OFF, marker, format, *params)
    }

    fun off(message: Message) {
        off(MOD_MARKER, message)
    }

    fun off(`object`: Any) {
        off(MOD_MARKER, `object`)
    }

    fun off(message: String) {
        off(MOD_MARKER, message)
    }

    fun off(format: String, vararg params: Any) {
        off(MOD_MARKER, format, *params)
    }

    // TRACE

    fun trace(marker: Marker, message: Message) {
        log(Level.TRACE, marker, message)
    }

    fun trace(marker: Marker, `object`: Any) {
        log(Level.TRACE, marker, `object`)
    }

    fun trace(marker: Marker, message: String) {
        log(Level.TRACE, marker, message)
    }

    fun trace(marker: Marker, format: String, vararg params: Any) {
        log(Level.TRACE, marker, format, *params)
    }

    fun trace(message: Message) {
        trace(MOD_MARKER, message)
    }

    fun trace(`object`: Any) {
        trace(MOD_MARKER, `object`)
    }

    fun trace(message: String) {
        trace(MOD_MARKER, message)
    }

    fun trace(format: String, vararg params: Any) {
        trace(MOD_MARKER, format, *params)
    }

    // WARN

    fun warn(marker: Marker, message: Message) {
        log(Level.WARN, marker, message)
    }

    fun warn(marker: Marker, `object`: Any) {
        log(Level.WARN, marker, `object`)
    }

    fun warn(marker: Marker, message: String) {
        log(Level.WARN, marker, message)
    }

    fun warn(marker: Marker, format: String, vararg params: Any) {
        log(Level.WARN, marker, format, *params)
    }

    fun warn(message: Message) {
        warn(MOD_MARKER, message)
    }

    fun warn(`object`: Any) {
        warn(MOD_MARKER, `object`)
    }

    fun warn(message: String) {
        warn(MOD_MARKER, message)
    }

    fun warn(format: String, vararg params: Any) {
        warn(MOD_MARKER, format, *params)
    }
}