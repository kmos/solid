package io.mosfet.solid.env

import liquibase.database.Database

interface SolidContainer {
    fun generate(): Database
}