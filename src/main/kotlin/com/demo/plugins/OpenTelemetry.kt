package com.demo.plugins

import io.ktor.server.application.*
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.extension.trace.propagation.B3Propagator
import io.opentelemetry.instrumentation.ktor.v2_0.server.KtorServerTracing
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.semconv.ResourceAttributes

fun Application.configureOpenTelemetry() {
    val applicationName = environment.config.property("ktor.application.name").getString()
    val traceEndpoint = environment.config.property("observability.tracing.endpoint").getString()

    val resource = Resource.getDefault().merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, applicationName)))
    val spanProcessor = BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().setEndpoint(traceEndpoint).build()).build()
    val openTelemetry = OpenTelemetrySdk.builder()
        .setPropagators(ContextPropagators.create(B3Propagator.injectingMultiHeaders()))
        .setTracerProvider(SdkTracerProvider.builder().addSpanProcessor(spanProcessor).addResource(resource).build())
        .buildAndRegisterGlobal()

    install(KtorServerTracing) {
        setOpenTelemetry(openTelemetry)
    }
}