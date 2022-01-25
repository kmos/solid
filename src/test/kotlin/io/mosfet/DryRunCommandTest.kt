package io.mosfet

import arrow.core.right
import io.mosfet.solid.core.commands.DryRunCommand
import io.mosfet.solid.core.commands.MigrationFailed
import io.mosfet.solid.core.commands.Ok
import io.mosfet.solid.core.SolidParameters
import io.mosfet.solid.core.commands.CommandHandler
import io.mosfet.solid.core.configuration.ConfigurationDatabaseChangeLog
import io.mosfet.solid.db.SolidDataSource
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.FileSystemResourceAccessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.io.File
import java.util.Properties

@Testcontainers
internal class DryRunCommandTest {

    private lateinit var solidDataSource: SolidDataSource
    private lateinit var commandHandler: CommandHandler
    private lateinit var underTest: DryRunCommand

    @BeforeEach
    internal fun setUp() {
        assertThat(container.isRunning).isTrue

        solidDataSource = SolidDataSource.getInstance(properties(container))

        commandHandler = CommandHandler(DatabaseFactory
            .getInstance()
            .findCorrectDatabaseImplementation(JdbcConnection(solidDataSource.connection)),
            FileSystemResourceAccessor(File(System.getProperty("user.dir")))
        )

        underTest = DryRunCommand(commandHandler, ConfigurationDatabaseChangeLog())
    }

    @Test
    internal fun `given an environment and a path with working changeset, when executing a dry-run, return no errors`() {

        val result = underTest.execute(SolidParameters(mapOf("path" to "./src/test/resources/working_changesets")))

        val resultSet = solidDataSource
            .connection
            .prepareStatement(QUERY_TEST)
            .executeQuery()

        resultSet.next()

        assertThat(result).isEqualTo(Ok.right())
        assertThat(resultSet.getInt(1)).isEqualTo(9)
    }

    @Test
    internal fun `given an environment and a path with broken changeset, when executing a dry-run, return an error`() {

        val input = SolidParameters(mapOf("path" to "./src/test/resources/broken_changesets"))

        underTest.execute(input)
            .tap { error("unreachable state") }
            .tapLeft {
                when (it) {
                    is MigrationFailed -> assertThat(it.solidParameters).isEqualTo(input)
                    else -> error("unreachable state")
                }
            }

    }

    companion object {
        @Container
        private val container = MySQLContainer(DockerImageName.parse("mysql:8.0.28")
            .asCompatibleSubstituteFor("mysql"))

        private const val QUERY_TEST = "select count(*) from information_schema.TABLES where TABLE_SCHEMA='test'"

        private fun properties(container: MySQLContainer<out MySQLContainer<*>>): Properties {
            val properties = Properties()

            properties.setProperty("jdbcUrl", container.jdbcUrl)
            properties.setProperty("username", container.username)
            properties.setProperty("password", container.password)

            return properties
        }
    }
}