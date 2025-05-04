package com.sebqvcoding.springboot.auth.database.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document("refresh_token")
data class RefreshToken(
    val userId: ObjectId,
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant,
    val createdAt: Instant,
    val hashedToken: String
)