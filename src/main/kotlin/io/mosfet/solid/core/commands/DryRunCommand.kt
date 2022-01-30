package io.mosfet.solid.core.commands

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.mosfet.solid.core.SolidParameters
import io.mosfet.solid.core.configuration.ConfigurationDatabaseChangeLog
import liquibase.Liquibase
import liquibase.changelog.visitor.ChangeExecListener
import liquibase.exception.LiquibaseException

class DryRunCommand(
    private val commandHandler: CommandHandler,
    private val configurationDatabaseChangeLog: ConfigurationDatabaseChangeLog,
    private val listener: ChangeExecListener,
) : Command {
    override fun execute(solidParameters: SolidParameters): Either<CommandError, CommandResult> {

        val liquibase = Liquibase(
            configurationDatabaseChangeLog.create(solidParameters.get("path")!!,
                commandHandler.resourceAccessor,
                commandHandler.database),
            commandHandler.resourceAccessor,
            commandHandler.database)

        liquibase.setChangeExecListener(listener)

        return try {
            liquibase.updateTestingRollback("")
            Ok.right()
        } catch (e: LiquibaseException) {
            MigrationFailed(e, solidParameters).left()
        }
    }

}


