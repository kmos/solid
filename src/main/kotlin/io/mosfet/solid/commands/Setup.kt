package io.mosfet.solid.commands

import java.util.logging.LogManager

class Setup {

    fun execute() {
        LogManager.getLogManager().reset()
    }
}