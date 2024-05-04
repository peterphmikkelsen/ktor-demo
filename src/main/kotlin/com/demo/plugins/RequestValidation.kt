package com.demo.plugins

import com.demo.users.api.ApiResource
import com.demo.users.api.CreateUserRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<ApiResource.Data<CreateUserRequest>> { it.data.validate() }
    }
}