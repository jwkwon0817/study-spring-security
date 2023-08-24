package me.jwkwon0817.security

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PasswordEncoderTest {
    @Test
    fun encode() {
        val encode = passwordEncoder().encode("1234")
        val encode2 = passwordEncoder().encode("1234")

        val matches = passwordEncoder().matches("1234", encode)
        val matches2 = passwordEncoder().matches("1234", encode2)

        println("matches = $matches")
        println("matches2 = $matches2")

        Assertions.assertEquals(matches, matches2)
    }

    private fun passwordEncoder() = BCryptPasswordEncoder()
}