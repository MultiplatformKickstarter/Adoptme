package com.multiplatformkickstarter.repository.profile

import com.multiplatformkickstarter.app.common.model.Profile

interface ProfileRepository {

    suspend fun addProfile(
        userId: Int,
        name: String,
        description: String?,
        image: String?,
        location: String?,
        rating: Double?
    ): Profile?

    suspend fun getProfile(userId: Int): Profile?

    suspend fun updateProfile(
        userId: Int,
        name: String?,
        description: String?,
        image: String?,
        location: String?,
        rating: Double?
    ): Profile?
}
