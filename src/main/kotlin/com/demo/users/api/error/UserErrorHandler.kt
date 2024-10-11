package com.demo.users.api.error

import com.demo.users.UserNotFoundException
import com.demo.users.api.ApiResource
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.userErrorHandler() {
    exception<UserNotFoundException> { call, cause ->
        val status = HttpStatusCode.NotFound
        call.response.status(status)
        call.respond(ApiResource.Error("user with id: ${cause.id} does not exist!", status.value))
    }
}