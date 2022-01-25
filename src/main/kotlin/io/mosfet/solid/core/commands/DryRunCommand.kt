package io.mosfet.solid.core.commands

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.mosfet.solid.core.SolidParameters
import io.mosfet.solid.core.configuration.ConfigurationDatabaseChangeLog
import liquibase.Liquibase
import liquibase.exception.LiquibaseException

class DryRunCommand(
    private val commandHandler: CommandHandler,
    private val configurationDatabaseChangeLog: ConfigurationDatabaseChangeLog,
) : Command {
    override fun execute(solidParameters: SolidParameters): Either<CommandError, CommandResult> {

        val liquibase = Liquibase(
            configurationDatabaseChangeLog.create(solidParameters.get("path")!!,
                commandHandler.resourceAccessor,
                commandHandler.database),
            commandHandler.resourceAccessor,
            commandHandler.database)

        return try {
            liquibase.updateTestingRollback("")
            Ok.right()
        } catch (e: LiquibaseException) {
            MigrationFailed(e, solidParameters).left()
        }
    }

}


