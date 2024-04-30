package com.testprojectkotlin.controller.dto

data class ClientDto (
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val job: String? = null,
    val position: String? = null,
    val gender: String? = null
)