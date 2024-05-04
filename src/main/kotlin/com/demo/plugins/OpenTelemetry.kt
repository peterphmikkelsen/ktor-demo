package com.demo.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.extension.trace.propagation.B3Propagator
import io.opentelemetry.instrumentation.api.instrumenter.SpanKindExtractor
import io.opentelemetry.instrumentation.ktor.v2_0.server.KtorServerTracing
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.semconv.ResourceAttributes

fun Application.configureOpenTelemetry() {
    val applicationName = environment.config.property("ktor.application.name").getString()
    val traceEndpoint = environment.config.property("observability.tracing.endpoint").getString()

    val openTelemetry = createAndRegisterOpenTelemetrySdk(applicationName, traceEndpoint)
    install(KtorServerTracing) {
        setOpenTelemetry(openTelemetry)
        capturedRequestHeaders(HttpHeaders.UserAgent)

        spanKindExtractor {
            if (httpMethod == HttpMethod.Post) {
                SpanKind.PRODUCER
            } else {
                SpanKind.CLIENT
            }
        }
    }
}

private fun createAndRegisterOpenTelemetrySdk(applicationName: String, traceEndpoint: String): OpenTelemetry {
    val resource = Resource.getDefault().merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, applicationName)))
    val spanProcessor = BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().setEndpoint(traceEndpoint).build()).build()
    return OpenTelemetrySdk.builder()
        .setPropagators(ContextPropagators.create(B3Propagator.injectingMultiHeaders()))
        .setTracerProvider(SdkTracerProvider.builder().addSpanProcessor(spanProcessor).addResource(resource).build())
        .buildAndRegisterGlobal()
}

private fun KtorServerTracing.Configuration.capturedRequestHeaders(vararg headers: String) {
    setCapturedRequestHeaders(headers.toList())
}

fun KtorServerTracing.Configuration.spanKindExtractor(extract: ApplicationRequest.() -> SpanKind) {
    setSpanKindExtractor {
        SpanKindExtractor<ApplicationRequest> { request: ApplicationRequest ->
            extract(request)
        }
    }
}