package com.demo.users.api

import com.demo.users.User
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val age: Int,
    val phoneNumber: String? = null
) {
    fun asDomain() = User(name, email, age, phoneNumber)
}