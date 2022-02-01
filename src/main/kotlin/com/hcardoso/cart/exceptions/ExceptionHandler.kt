package com.hcardoso.cart.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleGenericException(exception: Exception) = ResponseEntity(
        ApiErrorResponse(exception.message),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadableException(exception: HttpMessageNotReadableException) = ResponseEntity(
        ApiErrorResponse(null),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(exception: ProductNotFoundException) = ResponseEntity(
        ApiErrorResponse("Product not found"),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(CartNotFoundException::class)
    fun handleCartNotFoundException(exception: CartNotFoundException) = ResponseEntity(
        ApiErrorResponse("Cart not found"),
        HttpStatus.BAD_REQUEST
    )
}