package io.mosfet.solid.core.commands

import liquibase.database.Database
import liquibase.resource.ResourceAccessor

data class CommandHandler(val database: Database, val resourceAccessor: ResourceAccessor)
