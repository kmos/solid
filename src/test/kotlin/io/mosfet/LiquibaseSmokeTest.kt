package io.mosfet;

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.ContextExpression
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.DatabaseChangeLog
import liquibase.database.Database
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

@Testcontainers
class LiquibaseSmokeTest{

    @Container
    private val container = MySQLContainer(DockerImageName.parse("mysql:8.0.28").asCompatibleSubstituteFor("mysql"))
    private val config = HikariConfig()
    private val changeLog = DatabaseChangeLog()
    private lateinit var database: Database
    private lateinit var dataSource: HikariDataSource

    private val projectDir = File(System.getProperty("user.dir"))

    @BeforeEach
    internal fun setUp() {
        assertThat(container.isRunning).isTrue

        config.jdbcUrl = container.jdbcUrl
        config.username = container.username
        config.password = container.password
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        dataSource = HikariDataSource(config)

        database = DatabaseFactory
            .getInstance()
            .findCorrectDatabaseImplementation(JdbcConnection(dataSource.connection))

        changeLog.changeLogParameters = ChangeLogParameters(database)
        changeLog.includeAll(
            "./src/test/resources/working_changesets",
            true,
            null,
            false,
            null,
            FileSystemResourceAccessor(projectDir),
            ContextExpression(""),
            LabelExpression(),
            false
        )
    }

    @Test
    fun ` given a set of changeset, check if they are applicable to a mysql container`() {
        val liquibase = Liquibase(changeLog, FileSystemResourceAccessor(projectDir), database)

        liquibase.update("")

        val resultSet = dataSource
            .connection
            .prepareStatement(QUERY_TEST)
            .executeQuery()

        resultSet.next()
        assertThat(resultSet.getInt(1)).isEqualTo(9)
    }

    companion object {
        private const val QUERY_TEST = "select count(*) from information_schema.TABLES where TABLE_SCHEMA='test'"
    }
}