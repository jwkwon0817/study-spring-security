package me.jwkwon0817.security.global.exception.handler

import me.jwkwon081.chat.global.exception.type.DuplicateEmailException
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(DuplicateEmailException::class)
    protected fun handleDuplicateEmailException(e: DuplicateEmailException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.")
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body("이메일 또는 비밀번호가 일치하지 않습니다.")
    }
}