package io.mosfet.solid.commands

import arrow.core.Either
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.parameters.arguments.argument
import io.mosfet.solid.core.SolidParameters
import io.mosfet.solid.core.commands.CommandHandler
import io.mosfet.solid.core.commands.DryRunCommand
import io.mosfet.solid.core.configuration.ConfigurationDatabaseChangeLog
import io.mosfet.solid.core.listener.ChangeSetListener
import io.mosfet.solid.core.parser.DefaultParser
import io.mosfet.solid.env.MySQLSolidContainer
import io.mosfet.solid.logger.EchoLogger
import io.mosfet.solid.validator.CheckInputPath
import liquibase.resource.FileSystemResourceAccessor
import java.io.File

class DryRunCliktCommand : CliktCommand() {

    private val logger = EchoLogger(this::echo)
    val path: String by argument(help="path for changesets")

    override fun run() {
        CheckInputPath().check(path)
            ?.let { throw it }

        Setup().execute()

        val solidPath = DefaultParser(path).parse()

        val command = DryRunCommand(
            CommandHandler(
                MySQLSolidContainer().generate(),
                FileSystemResourceAccessor(File(solidPath.absolute))),
            ConfigurationDatabaseChangeLog(),
            ChangeSetListener { logger.handle(it) }
        )

        when (command.execute(SolidParameters(mapOf("path" to solidPath.relative)))) {
            is Either.Left -> PrintMessage("Impossible to execute a dry-run with, please check input value.")
            is Either.Right -> echo("dry-run completed.")
        }

    }

}