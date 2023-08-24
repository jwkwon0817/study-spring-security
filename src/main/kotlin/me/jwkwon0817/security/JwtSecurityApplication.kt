package me.jwkwon0817.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtSecurityApplication

fun main(args: Array<String>) {
    runApplication<JwtSecurityApplication>(*args)
}
