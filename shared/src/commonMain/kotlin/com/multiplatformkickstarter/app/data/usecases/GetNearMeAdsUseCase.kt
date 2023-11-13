package com.multiplatformkickstarter.app.data.usecases

import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.common.model.SearchModel
import com.multiplatformkickstarter.app.common.usecases.UseCase
import com.multiplatformkickstarter.app.data.repositories.NearMeAdsMockRepository
import com.multiplatformkickstarter.app.data.repositories.ProfileRepository
import com.multiplatformkickstarter.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.multiplatformkickstarter.app.feature.search.repositories.PetsFromSearchRepository

class GetNearMeAdsUseCase(
    private val profileRepository: ProfileRepository,
    private val petsFromSearchRepository: PetsFromSearchRepository,
    private val nearMeAdsMockRepository: NearMeAdsMockRepository,
    private val globalAppSettingsRepository: GlobalAppSettingsRepository
) : UseCase() {

    suspend fun invoke(): Result<List<PetModel>> = runCatching {
        val location = profileRepository.getLocation()
        return@runCatching if (globalAppSettingsRepository.isMockedContentEnabled()) {
            nearMeAdsMockRepository.getAds().getOrDefault(emptyList())
        } else if (location != null) {
            val searchModel = SearchModel(location = location)
            petsFromSearchRepository.getPets(searchModel).pets
        } else {
            emptyList()
        }
    }
}
