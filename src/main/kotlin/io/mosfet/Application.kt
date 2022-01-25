package io.mosfet;

import io.mosfet.solid.commands.DryRunCliktCommand

class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DryRunCliktCommand().main(args)
        }
    }
}

