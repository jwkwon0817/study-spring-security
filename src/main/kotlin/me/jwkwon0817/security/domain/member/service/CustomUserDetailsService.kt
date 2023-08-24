package me.jwkwon0817.security.domain.member.service

import me.jwkwon0817.security.domain.member.entity.MemberDetails
import me.jwkwon0817.security.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        
        return MemberDetails(member)
    }
}