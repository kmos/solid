package io.mosfet.solid.core.commands

import arrow.core.Either
import io.mosfet.solid.core.SolidParameters

interface Command {
    fun execute(solidParameters: SolidParameters): Either<CommandError, CommandResult>
}

sealed class CommandError
data class MigrationFailed(val exception: Throwable, val solidParameters: SolidParameters): CommandError()
sealed class CommandResult
object Ok: CommandResult()