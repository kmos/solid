package io.mosfet.solid.validator

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class PathValidator(private val path: String): Validator {

    override fun validate(): Either<ValidationError, Ok> {

        val file = File(path)
        if(!file.isDirectory) return NotAFolder.left()
        file.parentFile ?: return Root.left()
        return checkEmpty(file.toPath())

    }

    private fun checkEmpty(directory: Path): Either<ValidationError, Ok> =
        try {
            Files.newDirectoryStream(directory).iterator().hasNext()
                .takeIf { it }
                ?.let { Ok.right() }
                ?: Empty.left()

        } catch (e: Exception) {
            FatalError.left()
        }

}

interface Validator {
    fun validate(): Either<ValidationError, Ok>
}

sealed class ValidationError
object NotAFolder : ValidationError()
object Empty : ValidationError()
object Root : ValidationError()
object FatalError : ValidationError()
object Ok