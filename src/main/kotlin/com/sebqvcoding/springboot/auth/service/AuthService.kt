package com.sebqvcoding.springboot.auth.service

import com.sebqvcoding.springboot.security.HashEncoder
import com.sebqvcoding.springboot.security.JwtService
import com.sebqvcoding.springboot.auth.core.TokenPair
import com.sebqvcoding.springboot.auth.database.model.RefreshToken
import com.sebqvcoding.springboot.auth.database.model.User
import com.sebqvcoding.springboot.auth.database.repository.RefreshTokenRepository
import com.sebqvcoding.springboot.auth.database.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.security.MessageDigest
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
    private val hashEncoder: HashEncoder
) {
    fun register(email: String, password: String): User =
        userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        )

    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw IllegalArgumentException("Invalid token.")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            IllegalArgumentException("No such user.")
        }

        val hashedToken = hashToken(refreshToken)

        refreshTokenRepository.findByUserIdAndHashedToken(ObjectId(userId), hashedToken)
            ?: throw IllegalArgumentException(
                "No such token, or token expired"
            )

        refreshTokenRepository.deleteByUserIdAndHashedToken(ObjectId(userId), hashedToken)

        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        saveRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    fun login(email: String, rawPassword: String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw BadCredentialsException("Invalid credentials.")

        if (!hashEncoder.matches(rawPassword, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        saveRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun saveRefreshToken(userId: ObjectId, refreshToken: String) {
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = jwtService.getExpirationFromToken(refreshToken).toInstant(),
                createdAt = jwtService.getIssuedAtFromToken(refreshToken).toInstant(),
                hashedToken = hashToken(refreshToken)
            )
        )
    }

    private fun hashToken(rawToken: String): String {
        val hashedToken = MessageDigest.getInstance("SHA-256").digest(rawToken.toByteArray())

        return Base64.getEncoder().encodeToString(hashedToken)
    }
}

