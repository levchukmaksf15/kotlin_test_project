package com.testprojectkotlin.exception

import org.springframework.http.HttpStatus

class NotUniqueEmailException(message: String) : ClientException(message) {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.NOT_ACCEPTABLE
    }
}