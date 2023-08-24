package me.jwkwon0817.security.domain.member.entity

import jakarta.persistence.*
import me.jwkwon0817.security.domain.member.dto.MemberDtoRes
import me.jwkwon0817.security.global.entity.BaseTime

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @Column(nullable = false, updatable = false, unique = true) val email: String,

    @Column(nullable = false, length = 10) val nickname: String,

    @Column(nullable = false) val password: String,
) : BaseTime() {

    fun toDto(): MemberDtoRes {
        return MemberDtoRes(
            id = this.id, email = this.email, nickname = this.nickname
        )
    }
}