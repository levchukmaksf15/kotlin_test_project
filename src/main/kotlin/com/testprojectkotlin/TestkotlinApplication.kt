package com.testprojectkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class TestkotlinApplication

fun main(args: Array<String>) {
	runApplication<TestkotlinApplication>(*args)
}
