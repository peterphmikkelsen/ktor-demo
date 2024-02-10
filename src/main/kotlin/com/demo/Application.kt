package com.demo

import com.demo.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureOpenAPI()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureErrorHandling()
    configureOpenTelemetry()
    configureKoin()
}
