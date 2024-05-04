package com.demo.utils.validation

import io.ktor.server.plugins.requestvalidation.*

fun interface Validation {
    fun validate(): ValidationResult
}