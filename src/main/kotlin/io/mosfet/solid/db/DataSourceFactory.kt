package io.mosfet.solid.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.Properties


/**
 * https://www.baeldung.com/hikaricp
 */
class SolidDataSource(properties: Properties): HikariDataSource(HikariConfig(properties)) {
    companion object : Holder<SolidDataSource, Properties>(::SolidDataSource)
}