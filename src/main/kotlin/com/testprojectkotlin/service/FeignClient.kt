package com.testprojectkotlin.service

import com.testprojectkotlin.controller.dto.FeignResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "genderize", url = "https://api.genderize.io")
fun interface FeignClient {

    @GetMapping
    fun getGender(@RequestParam(value = "name") name: String) : FeignResponseDto
}