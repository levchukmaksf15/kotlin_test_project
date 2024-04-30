package com.testprojectkotlin.exception

import org.springframework.http.HttpStatus

class GenderNotDetectedException(message: String) : ClientException(message) {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.BAD_REQUEST
    }
}