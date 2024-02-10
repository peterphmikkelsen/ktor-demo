package com.demo.users.api

import kotlinx.serialization.Serializable

sealed interface ApiResource {

    @Serializable
    data class Data<T>(val data: T): ApiResource

    @Serializable
    data class Error(val errors: List<ErrorDetails>): ApiResource {

        constructor(title: String, code: Int): this(listOf(ErrorDetails(title, code)))

        @Serializable
        data class ErrorDetails(val title: String, val code: Int, val reason: String? = null)
    }
}