package io.mosfet.solid.env

import io.mosfet.solid.db.SolidDataSource
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.util.Properties

class MySQLSolidContainer(private val fullImageName: DockerImageName =
    DockerImageName.parse("mysql:8.0.28")
    .asCompatibleSubstituteFor("mysql")
) : SolidContainer {

    override fun generate(): Database {
        val container = MySQLContainer(fullImageName)
            .waitingFor(Wait.defaultWaitStrategy())

        container.start()

        val dataSource = SolidDataSource.getInstance(properties(container))

        return DatabaseFactory
            .getInstance()
            .findCorrectDatabaseImplementation(JdbcConnection(dataSource.connection))
    }

    private fun properties(container: MySQLContainer<out MySQLContainer<*>>): Properties {
        val properties = Properties()

        properties.setProperty("jdbcUrl", container.jdbcUrl)
        properties.setProperty("username", container.username)
        properties.setProperty("password", container.password)

        return properties
    }

}