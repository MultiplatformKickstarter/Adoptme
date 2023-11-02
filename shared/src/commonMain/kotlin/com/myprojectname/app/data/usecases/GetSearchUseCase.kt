package com.myprojectname.app.data.usecases

import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.common.usecases.UseCase

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
