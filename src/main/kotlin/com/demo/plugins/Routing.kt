package com.demo.plugins

import com.demo.users.api.configureUserRoutes
import io.ktor.server.application.*

fun Application.configureRouting() {
    configureUserRoutes()
}
