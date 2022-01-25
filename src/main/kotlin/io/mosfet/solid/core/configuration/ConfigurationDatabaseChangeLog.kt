package io.mosfet.solid.core.configuration

import liquibase.ContextExpression
import liquibase.LabelExpression
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.DatabaseChangeLog
import liquibase.database.Database
import liquibase.resource.ResourceAccessor

class ConfigurationDatabaseChangeLog {

    fun create(
        path: String,
        fileSystemResourceAccessor: ResourceAccessor,
        database: Database,
    ): DatabaseChangeLog {

        val changeLog = DatabaseChangeLog()
        changeLog.changeLogParameters = ChangeLogParameters(database)
        changeLog.includeAll(
            path,
            true,
            null,
            false,
            null,
            fileSystemResourceAccessor,
            ContextExpression(""),
            LabelExpression(),
            false
        )

        return changeLog
    }
}