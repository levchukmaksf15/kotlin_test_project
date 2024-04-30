package com.testprojectkotlin.exception

import org.springframework.http.HttpStatus

class UnsupportedOperationException(message: String) : ClientException(message){
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.INTERNAL_SERVER_ERROR
    }
}