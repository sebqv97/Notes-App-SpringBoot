package com.sebqvcoding.springboot.auth.core

data class UserRequest(
    val email: String,
    val password: String,
    val id: String?
)