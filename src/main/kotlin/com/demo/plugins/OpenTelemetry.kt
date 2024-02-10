package com.demo.plugins

import io.ktor.server.application.*
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.instrumentation.ktor.v2_0.server.KtorServerTracing

fun Application.configureOpenTelemetry() {
    val openTelemetry = GlobalOpenTelemetry.get()

    install(KtorServerTracing) {
        setOpenTelemetry(openTelemetry)
    }
}