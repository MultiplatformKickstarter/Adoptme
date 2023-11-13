package com.multiplatformkickstarter.app.data.usecases

import com.multiplatformkickstarter.app.common.usecases.UseCase
import com.multiplatformkickstarter.app.data.repositories.ProfileRepository
import com.multiplatformkickstarter.app.data.repositories.SessionRepository

class LoadProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val sessionRepository: SessionRepository
) : UseCase() {

    suspend fun invoke(userId: Int) {
        if (sessionRepository.isLoggedIn()) {
            val profile = profileRepository.getProfile(userId)
            profileRepository.initProfile(userId, profile.name, profile.description, profile.image, profile.location, profile.rating)
        }
    }
}
