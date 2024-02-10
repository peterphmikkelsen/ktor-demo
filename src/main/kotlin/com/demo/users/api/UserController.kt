package com.demo.users.api

import com.demo.users.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureUserRoutes() {
    val userService by inject<UserService>()

    routing {
        route("/v1/user") {
            post {
                val user = call.receive<ApiResource.Data<CreateUserRequest>>().data.asDomain()
                val (id, created, status) = userService.createNewUser(user)
                call.respond(HttpStatusCode.Created, ApiResource.Data(UserReferenceResponse(id, created, status)))
            }

            get("/{id}") {
                val id = call.parameters["id"].uuid()
                val (user, lastModified, status) = userService.getUserById(id)
                call.respond(ApiResource.Data(UserResponse.from(user, lastModified, status)))
            }

            patch("/{id}") {
                val id = call.parameters["id"].uuid()
                userService.markUserAsInactive(id)
                call.response.status(HttpStatusCode.Accepted)
            }

            delete("/{id}") {
                val id = call.parameters["id"].uuid()
                userService.deleteUserById(id)
                call.response.status(HttpStatusCode.NoContent)
            }
        }
    }
}

private fun String?.uuid(): UUID {
    try {
        return UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        throw UUIDFormatException(this)
    }
}

class UUIDFormatException(val value: String?): Exception() {
    companion object {
        suspend fun handle(call: ApplicationCall, cause: UUIDFormatException) {
            val status = HttpStatusCode.BadRequest
            call.response.status(status)
            call.respond(ApiResource.Error("value ${cause.value} could not be converted to UUID", status.value))
        }
    }
}
