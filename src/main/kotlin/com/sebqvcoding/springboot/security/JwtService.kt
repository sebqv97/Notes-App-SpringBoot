package com.sebqvcoding.springboot.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("JWT_SECRET_BASE64") private val jwtSecret: String
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    private val accessTokenExpiryTimeInMs = 15L * 60L * 1000L

    private val refreshTokenExpiryTimeInMs = 30L * 24L * 60L * 60L * 1000L

    private fun generateToken(
        userId: String,
        type: TokenType,
        expiry: Long
    ): String {
        val now = Date()
        val expiration = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String):String{
      return  generateToken(userId, TokenType.ACCESS_TOKEN, accessTokenExpiryTimeInMs)
    }

    fun generateRefreshToken(userId: String):String{
        return  generateToken(userId, TokenType.REFRESH_TOKEN, refreshTokenExpiryTimeInMs)
    }
}

enum class TokenType {
    ACCESS_TOKEN,
    REFRESH_TOKEN
}
