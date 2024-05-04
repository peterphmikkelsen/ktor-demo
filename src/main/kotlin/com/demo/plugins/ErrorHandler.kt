package com.demo.plugins

import com.demo.users.api.ApiResource
import com.demo.users.api.error.userErrorHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        userErrorHandler()

        exception<MissingRequestParameterException> { call, cause ->
            call.respondText("missing \"${cause.parameterName}\" parameter", status = HttpStatusCode.BadRequest)
        }

        exception<RequestValidationException> { call, cause ->
            val status = HttpStatusCode.BadRequest
            val reasons = cause.reasons.map { ApiResource.Error.ErrorDetails(status.description, it) }
            call.respond(status, ApiResource.Error(reasons, status.value))
        }
    }
}