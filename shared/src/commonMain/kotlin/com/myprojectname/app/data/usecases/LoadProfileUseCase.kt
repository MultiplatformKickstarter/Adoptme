package com.myprojectname.app.data.usecases

import com.myprojectname.app.common.usecases.UseCase
import com.myprojectname.app.data.repositories.ProfileRepository
import com.myprojectname.app.data.repositories.SessionRepository

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
