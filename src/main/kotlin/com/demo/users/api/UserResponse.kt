package com.demo.users.api

import com.demo.users.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val age: Int,
    val phoneNumber: String? = null,
    val meta: Meta
) {
    companion object {
        fun from(user: User, lastModified: String, status: String): UserResponse {
            return UserResponse(user.name, user.email, user.age, user.phoneNumber, Meta(lastModified, status))
        }
    }

    @Serializable
    data class Meta(val lastModified: String, val status: String)
}