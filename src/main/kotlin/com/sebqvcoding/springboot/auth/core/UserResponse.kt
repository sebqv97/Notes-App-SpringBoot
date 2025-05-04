package com.sebqvcoding.springboot.auth.core

import java.time.Instant

data class UserResponse(
    val email:String,
    val hashedPassword:String
)