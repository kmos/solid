package io.mosfet.solid.logger

import io.mosfet.solid.core.listener.Execution

interface Logger {
    fun handle(data: Execution)
}