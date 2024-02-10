package com.demo.users.storage

import com.demo.users.Status
import com.demo.users.User
import com.demo.users.UserNotFoundException
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

class UserRepository {

    @WithSpan
    fun insert(user: User, initialStatus: Status = Status.Active): ResultRow = transaction {
        UserTable.insert {
            it[email] = user.email
            it[name] = user.name
            it[age] = user.age
            it[phoneNumber] = user.phoneNumber
            it[status] = initialStatus.toString()
            it[updated] = LocalDateTime.now()
        }.resultedValues!!.first()
    }

    fun findById(id: UUID): ResultRow = transaction {
        UserTable.select { UserTable.id eq id }.firstOrNull() ?: throw UserNotFoundException(id)
    }

    fun markAsInactive(id: UUID) = transaction {
        if (UserTable.update({ UserTable.id eq id }) {
                it[status] = Status.Inactive.toString(); it[updated] = LocalDateTime.now() // manually update the updated column
        } <= 0) throw UserNotFoundException(id)
    }

    fun deleteById(id: UUID) = transaction {
        UserTable.deleteWhere { UserTable.id eq id }
    }
}