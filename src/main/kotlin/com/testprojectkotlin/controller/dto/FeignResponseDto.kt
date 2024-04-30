package com.testprojectkotlin.controller.dto

data class FeignResponseDto (
    val count: Int,
    val name: String,
    val gender: String?,
    val probability: Float
)