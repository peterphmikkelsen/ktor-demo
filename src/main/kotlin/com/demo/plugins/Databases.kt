package com.demo.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect
import org.jetbrains.exposed.sql.vendors.PostgreSQLNGDialect


fun Application.configureDatabases() {
    val config = environment.config

    val url = config.property("storage.jdbcURL").getString()
    val driver = config.property("storage.driverClassName").getString()
    val user = config.property("storage.user").getString()
    val password = config.propertyOrNull("storage.user")?.getString() ?: ""

    // flyway migration
    Flyway.configure()
        .driver(driver)
        .dataSource(url, user, password)
        .load()
        .migrate()

    // connect to the database
    Database.connect(
        url = url,
        user = user,
        password = password,
        driver = driver
    )
}
