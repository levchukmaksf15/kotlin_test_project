package com.testprojectkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestkotlinApplication

fun main(args: Array<String>) {
	runApplication<TestkotlinApplication>(*args)
}
