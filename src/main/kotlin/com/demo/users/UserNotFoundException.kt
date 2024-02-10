package com.demo.users

import com.demo.users.api.ApiResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.UUID

class UserNotFoundException(private val id: UUID): Exception() {
    companion object {
        suspend fun handle(call: ApplicationCall, cause: UserNotFoundException) {
            val status = HttpStatusCode.NotFound
            call.response.status(status)
            call.respond(ApiResource.Error("user with id: ${cause.id} does not exist!", status.value))
        }
    }
}