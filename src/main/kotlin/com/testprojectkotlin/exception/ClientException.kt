package com.testprojectkotlin.exception

import org.springframework.http.HttpStatus

abstract class ClientException(message: String) : RuntimeException(message) {

    abstract fun getHttpStatus(): HttpStatus
}