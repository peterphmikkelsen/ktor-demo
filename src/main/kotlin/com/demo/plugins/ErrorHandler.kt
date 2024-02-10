package com.demo.plugins

import com.demo.users.api.error.userErrorHandler
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        userErrorHandler()
    }
}