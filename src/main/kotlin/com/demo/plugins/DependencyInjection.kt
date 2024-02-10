package com.demo.plugins

import com.demo.users.service.UserService
import com.demo.users.storage.UserRepository
import io.ktor.server.application.*
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.Tracer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    singleOf(::UserRepository)
    singleOf(::UserService)
    single<Tracer> { GlobalOpenTelemetry.get().getTracer("ktor-demo") }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}