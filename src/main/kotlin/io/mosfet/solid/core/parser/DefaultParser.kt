package io.mosfet.solid.core.parser

import java.io.File

class DefaultParser(private val path: String) : Parser {

    override fun parse(): SolidPath = File(path)
        .takeIf { !it.isAbsolute }
        ?.let { SolidPath(path, System.getProperty("user.dir")) }
        ?: SolidPath("./${File(path).relativeTo(File(File(path).parent)).path}", File(path).parent)

}