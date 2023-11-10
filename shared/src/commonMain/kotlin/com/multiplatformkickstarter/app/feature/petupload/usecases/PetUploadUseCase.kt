package com.multiplatformkickstarter.app.feature.petupload.usecases

import com.multiplatformkickstarter.app.common.model.GeoLocation
import com.multiplatformkickstarter.app.common.model.PetAge
import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.common.model.PetGender
import com.multiplatformkickstarter.app.common.model.PetSize
import com.multiplatformkickstarter.app.common.model.PetStatus
import com.multiplatformkickstarter.app.common.usecases.UseCase
import com.multiplatformkickstarter.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.multiplatformkickstarter.app.feature.petupload.repositories.PetUploadPublishRepository
import kotlin.random.Random

class PetUploadUseCase(
    private val petUploadPublishRepository: PetUploadPublishRepository,
    private val globalAppSettingsRepository: GlobalAppSettingsRepository
) : UseCase() {

    suspend fun invoke(
        name: String,
        description: String,
        images: List<String>,
        category: PetCategory,
        location: GeoLocation,
        breed: String,
        color: String,
        age: PetAge,
        gender: PetGender,
        size: PetSize,
        status: PetStatus
    ): Result<Int> = runCatching {
        if (!globalAppSettingsRepository.isMockedContentEnabled()) {
            val result = petUploadPublishRepository.upload(
                name, description, images, category, location, breed, color, age, gender, size, status
            )
            return@runCatching result.id
        } else {
            return@runCatching Random(10000).nextInt()
        }
    }
}
