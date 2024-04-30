package com.testprojectkotlin.exception

import org.springframework.http.HttpStatus

class ClientNotFoundException(message: String) : ClientException(message) {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.NOT_FOUND
    }
}