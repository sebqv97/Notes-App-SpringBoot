package com.sebqvcoding.springboot.auth.controller

import com.sebqvcoding.springboot.auth.core.TokenPair
import com.sebqvcoding.springboot.auth.core.UserRequest
import com.sebqvcoding.springboot.auth.core.UserResponse
import com.sebqvcoding.springboot.auth.database.model.User
import com.sebqvcoding.springboot.auth.mapper.UserMapper
import com.sebqvcoding.springboot.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val userMapper: UserMapper
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody body: UserRequest): UserResponse {
        return authService.register(body.email, body.password).let { userMapper.mapToUserResponse(it) }
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody body: UserRequest): TokenPair {
        return authService.login(body.email, body.password)
    }
}