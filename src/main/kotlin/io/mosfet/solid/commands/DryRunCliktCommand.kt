package io.mosfet.solid.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.mosfet.solid.core.SolidParameters
import io.mosfet.solid.core.commands.CommandHandler
import io.mosfet.solid.core.commands.DryRunCommand
import io.mosfet.solid.core.configuration.ConfigurationDatabaseChangeLog
import io.mosfet.solid.core.parser.DefaultParser
import io.mosfet.solid.env.MySQLSolidContainer
import liquibase.resource.FileSystemResourceAccessor
import java.io.File

class DryRunCliktCommand : CliktCommand() {

    val path: String by argument(help="path for changesets")

    override fun run() {

        val solidPath = DefaultParser(path).parse()

        val command = DryRunCommand(
            CommandHandler(
                MySQLSolidContainer().generate(),
                FileSystemResourceAccessor(File(solidPath.absolute))),
            ConfigurationDatabaseChangeLog()
        )

        val result = command.execute(SolidParameters(mapOf("path" to solidPath.relative)))

    }
}