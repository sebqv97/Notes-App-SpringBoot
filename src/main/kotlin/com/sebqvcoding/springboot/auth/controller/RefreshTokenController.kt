package com.sebqvcoding.springboot.auth.controller

import com.sebqvcoding.springboot.auth.core.TokenPair
import com.sebqvcoding.springboot.auth.service.AuthService
import com.sebqvcoding.springboot.security.JwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/refresh")
class RefreshTokenController(
    private val authService: AuthService
) {
    @GetMapping
    fun refreshToken(@RequestBody refreshTokenBody: RefreshTokenBody): TokenPair {
        return authService.refresh(refreshToken = refreshTokenBody.refreshToken)
    }

    data class RefreshTokenBody(
        val refreshToken: String
    )
}