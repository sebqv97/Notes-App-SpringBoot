package com.sebqvcoding.springboot.auth.mapper

import com.sebqvcoding.springboot.security.HashEncoder
import com.sebqvcoding.springboot.auth.core.UserRequest
import com.sebqvcoding.springboot.auth.core.UserResponse
import com.sebqvcoding.springboot.auth.database.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val hashEncoder: HashEncoder
) {
    fun mapToUser(userRequest: UserRequest): User {
        return with(userRequest) {
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        }
    }

    fun mapToUserResponse(user: User): UserResponse{
      return  with(user){
          UserResponse(
              email = email,
              hashedPassword = hashedPassword
          )
      }
    }
}
