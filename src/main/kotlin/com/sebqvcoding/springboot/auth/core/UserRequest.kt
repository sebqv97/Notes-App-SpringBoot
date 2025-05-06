package com.sebqvcoding.springboot.auth.core

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class UserRequest(
    @field: Email(message = "Invalid email format.")
    val email: String,
    @field:Pattern(regexp = "<add regex>", message = "Password must be at least 9 characters long and contain one digit, an uppercase and lowercase character.")
    val password: String,
    val id: String?
)