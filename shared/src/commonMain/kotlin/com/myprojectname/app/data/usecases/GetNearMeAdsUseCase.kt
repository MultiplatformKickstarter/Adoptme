package com.myprojectname.app.data.usecases

import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.common.model.SearchModel
import com.myprojectname.app.common.usecases.UseCase
import com.myprojectname.app.data.repositories.NearMeAdsMockRepository
import com.myprojectname.app.data.repositories.ProfileRepository
import com.myprojectname.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.myprojectname.app.feature.search.repositories.PetsFromSearchRepository

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
