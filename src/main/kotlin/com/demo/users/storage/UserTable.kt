package com.demo.users.storage

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

// Exposed ORM
object UserTable: UUIDTable("users") {
    val name = varchar("name", length = 255)
    val age = integer("age")
    val email = varchar("email", length = 255)
    val phoneNumber = varchar("phone_number", length = 20).nullable()

    val status = varchar("status", length = 15)

    val created = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updated = datetime("updated_at").nullable()
}