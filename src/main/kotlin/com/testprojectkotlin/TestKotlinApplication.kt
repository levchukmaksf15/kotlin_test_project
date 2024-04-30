package com.testprojectkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class TestKotlinApplication

fun main(args: Array<String>) {
	runApplication<TestKotlinApplication>(*args)
}