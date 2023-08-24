package me.jwkwon0817.security.global.config

import me.jwkwon0817.security.global.filter.JwtAuthenticationFilter
import me.jwkwon0817.security.global.provider.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.httpBasic { basic ->
            basic.disable()
        }

        http.csrf { csrf ->
            csrf.disable()
        }
        http.sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.authorizeHttpRequests { requests ->
            requests.requestMatchers("/api/user/signup", "/api/user/signin").permitAll()
            requests.requestMatchers("/api/user/me").authenticated()
            requests.anyRequest().permitAll()
        }

        http.addFilterBefore(
            JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}