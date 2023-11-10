package com.multiplatformkickstarter.repository.user

import com.multiplatformkickstarter.models.DatabaseUser

interface UserRepository {
    suspend fun addUser(
        email: String,
        name: String,
        passwordHash: String
    ): DatabaseUser?

    suspend fun findUser(userId: Int): DatabaseUser?
    suspend fun findUserByEmail(email: String): DatabaseUser?
}
