package com.testprojectkotlin.exception

data class ErrorResponseBody(
    val status: Int,
    val message: String?
)