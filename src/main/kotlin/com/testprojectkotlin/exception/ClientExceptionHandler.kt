package com.testprojectkotlin.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ClientExceptionHandler {

    @ExceptionHandler
    fun handleClientException(clientException: ClientException): ResponseEntity<ErrorResponseBody> {
        val exceptionBody = ErrorResponseBody(
            status = clientException.getHttpStatus().value(),
            message = clientException.message
        )

        return ResponseEntity(exceptionBody, clientException.getHttpStatus())
    }
}