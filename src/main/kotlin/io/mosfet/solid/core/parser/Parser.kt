package io.mosfet.solid.core.parser

interface Parser {
    fun parse(): SolidPath
}

data class SolidPath(val relative: String, val absolute: String)