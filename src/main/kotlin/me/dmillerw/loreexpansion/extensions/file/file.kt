package me.dmillerw.loreexpansion.extensions.file

import java.io.File
import java.io.FilenameFilter

/**
 * @author dmillerw
 */

public fun File.makeDirectory() : File {
    if (!this.exists() || !this.isDirectory)
        this.mkdir()
    return this
}

public fun File.listFilesWithExtension(extension: String) : Array<File> {
    val filter = FilenameFilter { file, name -> name.endsWith(extension) }
    return this.listFiles(filter)
}