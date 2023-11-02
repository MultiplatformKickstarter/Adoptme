package com.myprojectname.app.data.usecases

import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.common.usecases.UseCase
import com.myprojectname.app.data.repositories.LastSearchAdsMockRepository
import com.myprojectname.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.myprojectname.app.feature.search.repositories.LastSearchesRepository
import com.myprojectname.app.feature.search.repositories.PetsFromSearchRepository

class GetLastSearchUseCase(
    private val lastSearchesRepository: LastSearchesRepository,
    private val petsFromSearchRepository: PetsFromSearchRepository,
    private val lastSearchAdsMockRepository: LastSearchAdsMockRepository,
    private val globalAppSettingsRepository: GlobalAppSettingsRepository
) : UseCase() {

    suspend fun invoke(): Result<List<PetModel>> = runCatching {
        val searchesList = lastSearchesRepository.get()
        return@runCatching if (globalAppSettingsRepository.isMockedContentEnabled()) {
            lastSearchAdsMockRepository.getAds().getOrDefault(emptyList())
        } else if (searchesList.list.isNotEmpty()) {
            petsFromSearchRepository.getPets(searchesList.list.last()).pets
        } else {
            emptyList()
        }
    }
}
