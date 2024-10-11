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
    // Dependency injection with Koin
    val userService by inject<UserService>()

    routing {
        route("/v1/user") {
            post {
                // validation? self-ownership vs. ownerless
                val user = call.receive<ApiResource.Data<CreateUserRequest>>().data.asDomain()
                val (id, created, status) = userService.createNewUser(user)
                call.respond(HttpStatusCode.Created, ApiResource.Data(UserReferenceResponse(id, created, status)))
            }

            get("/{id}") {
                // more like golang
                val id = call.parameters["id"].uuid().getOrNull()
                if (id == null) {
                    call.handleUUIDException()
                    return@get
                }

                // slightly more idiomatic
                val (user, lastModified, status) = userService.getUserById(id).getOrElse {
                    val status = HttpStatusCode.NotFound
                    call.response.status(status)
                    call.respond(ApiResource.Error("user with id: $id does not exist!", status.value))
                    return@get
                }

                call.respond(ApiResource.Data(UserResponse.from(user, lastModified, status)))
            }

            patch("/{id}") {
                val id = call.parameters["id"].uuid().getOrElse {
                    call.handleUUIDException()
                    return@patch
                }

                userService.markUserAsInactive(id)
                call.response.status(HttpStatusCode.Accepted)
            }

            delete("/{id}") {
                val id = call.parameters["id"].uuid().getOrElse {
                    call.handleUUIDException()
                    return@delete
                }

                userService.deleteUserById(id)
                call.response.status(HttpStatusCode.NoContent)
            }
        }
    }
}

private fun String?.uuid(): Result<UUID> {
    return Result.runCatching { UUID.fromString(this@uuid) }
}

suspend fun ApplicationCall.handleUUIDException() {
    val status = HttpStatusCode.BadRequest
    this.response.status(status)
    this.respond(ApiResource.Error("id is not a valid UUID", status.value))
}
