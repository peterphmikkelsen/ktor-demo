package com.demo.users.api.error

import com.demo.users.UserNotFoundException
import com.demo.users.api.ApiResource
import com.demo.users.api.UUIDFormatException
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.userErrorHandler() {
    exception(UserNotFoundException::handle)
    exception(UUIDFormatException::handle)

    exception<Exception> { call, _ ->
        val status = HttpStatusCode.InternalServerError
        call.response.status(status)
        call.respond(ApiResource.Error(status.description, status.value))
    }
}