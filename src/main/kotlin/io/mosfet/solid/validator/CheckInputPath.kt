package io.mosfet.solid.validator

import arrow.core.Either
import com.github.ajalt.clikt.core.PrintMessage

class CheckInputPath {

    fun check(path: String): PrintMessage? = when (val result = PathValidator(path).validate()) {
        is Either.Left -> printError(result.value)
        is Either.Right -> null
    }

    private fun printError(value: ValidationError): PrintMessage =
        when (value) {
            Empty -> PrintMessage("the directory is empty, exit...", true)
            FatalError -> PrintMessage("fatal error reading the directory, exit...", true)
            NotAFolder -> PrintMessage("the input is not a directory, exit...", true)
            Root -> PrintMessage("the root folder cannot be used as possibile input, exit...", true)
        }
}