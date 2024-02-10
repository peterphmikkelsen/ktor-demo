package com.demo.utils.tracing

import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import org.koin.java.KoinJavaComponent.inject

val tracer by inject<Tracer>(Tracer::class.java)

fun <T> traced(
    name: String,
    kind: SpanKind = SpanKind.INTERNAL,
    statement: () -> T
): T {
    val span = tracer.spanBuilder(name).setSpanKind(kind).run {
        startSpan()
    }

    return try {
        statement()
    } catch (throwable: Throwable) {
        span.setStatus(StatusCode.ERROR)
        span.recordException(throwable)
        throw throwable
    } finally {
        span.end()
    }
}