package com.multiplatformkickstarter.app.data.usecases

import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.common.usecases.UseCase

class GetSearchUseCase(
    private val nearMeAdsUseCase: GetNearMeAdsUseCase
) : UseCase() {

    suspend fun invoke(searchId: Int): Result<List<PetModel>> = runCatching {
        return@runCatching emptyList()
    }

    suspend fun invoke(petCategory: PetCategory): Result<List<PetModel>> = runCatching {
        return@runCatching nearMeAdsUseCase.invoke().getOrDefault<List<PetModel>, List<PetModel>>(emptyList<PetModel>())
            .filter<PetModel> { it.category == petCategory }
    }
}
