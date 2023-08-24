package me.jwkwon0817.security.global.provider

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import me.jwkwon0817.security.global.exception.type.InvalidTokenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*


const val ACCESS_TOKEN_VALID_TIME: Long = 1000 * 60

@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun generateAccessToken(authentication: Authentication): String {
        val authorities: String =
            authentication.authorities.joinToString(",", transform = GrantedAuthority::getAuthority)

        val now = Date()
        val accessExpiration = Date(now.time + ACCESS_TOKEN_VALID_TIME)

        return Jwts.builder().setSubject(authentication.name).claim("auth", authorities).setIssuedAt(now)
            .setExpiration(accessExpiration).signWith(key, SignatureAlgorithm.HS256).compact()
    }

    /**
     * Token 정보 추출
     */
    fun getAuthentication(token: String): Authentication {
        val claims: Claims = getClaims(token)

        val auth: String = claims["auth"] as String

        val authorities: Collection<GrantedAuthority> = auth.split(",").map {
            SimpleGrantedAuthority(it)
        }

        val principal: UserDetails = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    /**
     * Token 검증
     */
    fun validateAccessToken(token: String): Boolean =
        try {
            val claims: Claims = getClaims(token)
            claims["auth"] != null
        } catch (e: SecurityException) {
            throw InvalidTokenException("잘못된 토큰입니다.")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("잘못된 토큰입니다.")
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException("만료된 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("지원되지 않는 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("잘못된 토큰입니다.")
        }

    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
}