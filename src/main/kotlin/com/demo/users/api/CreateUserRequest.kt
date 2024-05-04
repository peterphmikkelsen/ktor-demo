package com.demo.users.api

import com.demo.users.User
import com.demo.utils.validation.Validation
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.requestvalidation.ValidationResult.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val age: Int,
    val phoneNumber: String? = null
): Validation {
    fun asDomain() = User(name, email, age, phoneNumber)

    override fun validate(): ValidationResult {
        val reasons = mutableListOf<String>()
        name.let {
            if (it.isBlank()) reasons.add("name cannot be blank")
            if (it.length > 50) reasons.add("name is too long")
        }

        email.let {
            if (it.isBlank()) reasons.add("email cannot be blank")
            if (!it.contains("@")) reasons.add("email is not correct format")
        }

        if (age < 0) reasons.add("age is too low")

        phoneNumber?.let {
            if (it.isBlank()) reasons.add("phone number cannot be blank")
            if (it.length != 8) reasons.add("phone number must be 8 digits")
        }

        if (reasons.isNotEmpty()) return Invalid(reasons)
        return Valid
    }
}