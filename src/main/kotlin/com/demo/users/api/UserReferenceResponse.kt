package com.demo.users.api

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class UserReferenceResponse(val reference: String, val meta: Meta) {
    constructor(reference: UUID, created: String, status: String) : this(reference.toString(), Meta(created, status))

    @Serializable
    data class Meta(val created: String, val status: String)
}