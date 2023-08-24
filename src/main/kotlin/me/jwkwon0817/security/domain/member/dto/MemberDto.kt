package me.jwkwon0817.security.domain.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class MemberDtoReq(
    @NotBlank @field:Email val email: String,
    @NotBlank val nickname: String,
    @NotBlank val password: String,
)

data class MemberDtoRes(
    val id: Long?,
    val email: String,
    val nickname: String,
)

data class LoginDtoReq(
    @NotBlank @field:Email val email: String,
    @NotBlank val password: String
)