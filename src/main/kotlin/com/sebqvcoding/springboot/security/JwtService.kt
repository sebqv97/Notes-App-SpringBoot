package com.sebqvcoding.springboot.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    fun generateAccessToken(userId: String): String {
        return generateToken(userId,"access token", accessTokenExpiryTimeInMs)
    }

    fun generateRefreshToken(userId: String): String {
        return generateToken(userId, "refresh token", refreshTokenExpiryTimeInMs)
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "access token"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "refresh token"
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token.")
        return claims.subject
    }

    fun getExpirationFromToken(token: String): Date {
        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token.")
        return claims.expiration
    }

    fun getIssuedAtFromToken(token: String): Date {
        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token.")
        return claims.issuedAt
    }

    private fun parseAllClaims(token: String): Claims? {
        return try {
            val rawToken = if (token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }

    private fun generateToken(
        userId: String,
        type: String,
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

    private companion object {
        const val accessTokenExpiryTimeInMs = 15L * 60L * 1000L
        const val refreshTokenExpiryTimeInMs = 30L * 24L * 60L * 60L * 1000L
    }
}
