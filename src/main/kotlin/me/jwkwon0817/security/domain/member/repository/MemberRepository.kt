package me.jwkwon0817.security.domain.member.repository

import me.jwkwon0817.security.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByEmail(email: String): Member?
    fun findByEmailAndPassword(email: String, password: String): Member?
}
