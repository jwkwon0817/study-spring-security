package me.jwkwon0817.security.domain.member.service

import jakarta.transaction.Transactional
import me.jwkwon081.chat.global.authority.TokenInfo
import me.jwkwon081.chat.global.exception.type.DuplicateEmailException
import me.jwkwon0817.security.domain.member.dto.LoginDtoReq
import me.jwkwon0817.security.domain.member.dto.MemberDtoReq
import me.jwkwon0817.security.domain.member.dto.MemberDtoRes
import me.jwkwon0817.security.domain.member.entity.Member
import me.jwkwon0817.security.domain.member.repository.MemberRepository
import me.jwkwon0817.security.global.provider.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {

    val logger: Logger = LoggerFactory.getLogger(MemberService::class.java)

    fun signUp(userDtoReq: MemberDtoReq): MemberDtoRes {
        var member: Member? = memberRepository.findByEmail(userDtoReq.email)

        if (member != null) {
            throw DuplicateEmailException("이미 존재하는 이메일입니다.")
        }

        member = Member(
            email = userDtoReq.email,
            nickname = userDtoReq.nickname,
            password = passwordEncoder.encode(userDtoReq.password)
        )

        logger.info("sign up password: ${member.password}")

        val save = memberRepository.save(member)

        return save.toDto()
    }

    /**
     * 로그인 -> 토큰 발행
     */
    fun signIn(loginDto: LoginDtoReq): TokenInfo {
        logger.info("sign in password (origin): ${loginDto.password}")
        logger.info("sign in password: ${passwordEncoder.encode(loginDto.password)}")

        val member = customUserDetailsService.loadUserByUsername(loginDto.email)

        val authenticationToken =
            UsernamePasswordAuthenticationToken(member.username, member.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)


        val accessToken = jwtTokenProvider.generateAccessToken(authentication)

        return TokenInfo("Bearer", accessToken)
    }
}