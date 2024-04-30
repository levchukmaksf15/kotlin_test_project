package com.testprojectkotlin.controller.dto

import java.util.UUID

data class ClientDto (
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val job: String? = null,
    val position: String? = null,
    val gender: String? = null
)