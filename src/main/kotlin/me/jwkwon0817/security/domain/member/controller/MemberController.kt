package me.jwkwon0817.security.domain.member.controller

import jakarta.validation.Valid
import me.jwkwon0817.security.domain.member.dto.LoginDtoReq
import me.jwkwon0817.security.domain.member.dto.MemberDtoReq
import me.jwkwon0817.security.domain.member.entity.MemberDetails
import me.jwkwon0817.security.domain.member.service.MemberService
import me.jwkwon0817.security.global.provider.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/member")
@RestController
@CrossOrigin(origins = ["*"])
class MemberController(
    private val memberService: MemberService,
    private val tokenProvider: JwtTokenProvider
) {

    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid userDtoReq: MemberDtoReq,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.allErrors)
        }

        val memberDtoRes = memberService.signUp(userDtoReq)

        return ResponseEntity.ok(memberDtoRes)
    }

    @PostMapping("/signin")
    fun signIn(
        @RequestBody @Valid loginDto: LoginDtoReq,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.allErrors)
        }

        val tokenInfo = memberService.signIn(loginDto)

        return ResponseEntity.ok(tokenInfo)
    }

    @GetMapping("/me")
    fun me(): ResponseEntity<MemberDetails> {
        val me = SecurityContextHolder.getContext().authentication.principal as? MemberDetails

        return ResponseEntity.ok(me)
    }
}