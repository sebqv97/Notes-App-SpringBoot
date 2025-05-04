package com.sebqvcoding.springboot.auth.database.repository

import com.sebqvcoding.springboot.auth.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}