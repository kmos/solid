package io.mosfet.solid.core

class SolidParameters(private val parameters: Map<String, String>) {
    fun get(key: String): String? {
        return parameters[key]
    }
}
