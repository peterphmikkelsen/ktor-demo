package com.demo.users.service

import com.demo.users.User
import com.demo.users.storage.UserRepository
import com.demo.users.storage.UserTable
import com.demo.utils.tracing.traced
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class UserService(private val userRepository: UserRepository) {

    fun createNewUser(user: User): Triple<UUID, String, String> {
        val row = userRepository.insert(user)
        val id = row[UserTable.id].value
        val created = row[UserTable.created].toString()
        val status = row[UserTable.status].toString()
        return Triple(id, created, status)
    }

    fun getUserById(id: UUID): Result<Triple<User, String, String>> {
        val row = userRepository.findById(id).getOrElse { return Result.failure(it) }
        val lastModified = row[UserTable.updated].toString()
        val status = row[UserTable.status].toString()
        return Result.success(Triple(row.toDomain(), lastModified, status))
    }

    fun markUserAsInactive(id: UUID) = traced {
        userRepository.markAsInactive(id)
    }

    fun deleteUserById(id: UUID) = traced {
        userRepository.deleteById(id)
    }

    private fun ResultRow.toDomain() = User(
        name = this[UserTable.name],
        email = this[UserTable.email],
        age = this[UserTable.age],
        phoneNumber = this[UserTable.phoneNumber]
    )
}