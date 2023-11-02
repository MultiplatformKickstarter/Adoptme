package com.myprojectname.app.feature.petupload.usecases

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetAge
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetGender
import com.myprojectname.app.common.model.PetSize
import com.myprojectname.app.common.model.PetStatus
import com.myprojectname.app.common.usecases.UseCase
import com.myprojectname.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.myprojectname.app.feature.petupload.repositories.PetUploadPublishRepository
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
