package com.sebqvcoding.springboot.auth.core

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)