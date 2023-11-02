package com.myprojectname.app.feature.petupload.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetAge
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetGender
import com.myprojectname.app.common.model.PetSize
import com.myprojectname.app.common.model.PetStatus
import com.myprojectname.app.data.repositories.UnauthorizedException
import com.myprojectname.app.feature.petupload.usecases.PetUploadUseCase
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.platform.MyProjectNameEventTracker
import com.myprojectname.app.platform.RootNavigatorRepository
import com.myprojectname.app.platform.TrackEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val MINIMUM_NAME_CHARS = 3
private const val MINIMUM_DESCRIPTION_CHARS = 3

class PetUploadViewModel(
    private val localization: Localization,
    private val rootNavigatorRepository: RootNavigatorRepository,
    private val petUploadUseCase: PetUploadUseCase,
    private val eventTracker: MyProjectNameEventTracker
) : ScreenModel {
    private val _state = MutableStateFlow(
        PetUploadState(
            name = "",
            description = "",
            images = emptyList(),
            category = PetCategory.DOGS,
            location = GeoLocation(0.0, 0.0),
            breed = "",
            color = "",
            age = PetAge.ADULT,
            gender = PetGender.MALE,
            size = PetSize.MEDIUM,
            status = PetStatus.ADOPTABLE,
            isNameError = false,
            nameSupportingText = "",
            isDescriptionError = false,
            descriptionSupportingText = ""
        )
    )
    val state = _state.asStateFlow()
    private val _sideEffects = Channel<PetUploadSideEffects>()
    val sideEffects: Flow<PetUploadSideEffects> = _sideEffects.receiveAsFlow()

    fun onSubmit(
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
    ) {
        resetErrorValues()
        basicValuesCheck(name, description)
        screenModelScope.launch {
            petUploadUseCase.invoke(name, description, images, category, location, breed, color, age, gender, size, status)
                .onSuccess {
                    eventTracker.onEventTracked(TrackEvents.PET_UPLOAD_SUCCESSFUL)
                    _sideEffects.trySend(PetUploadSideEffects.OnSubmitted)
                }.onFailure {
                    eventTracker.onEventTracked(TrackEvents.PET_UPLOAD_ERROR)
                    when (it) {
                        is UnauthorizedException -> _sideEffects.trySend(PetUploadSideEffects.OnSubmittedError(localization.genericFailedDefaultMessage))
                        else -> _sideEffects.trySend(PetUploadSideEffects.OnSubmittedError(localization.genericFailedDefaultMessage))
                    }
                    Logger.w(it) { "Error on Uploading Pet" }
                }
        }
    }

    fun onPetUploaded() {
        rootNavigatorRepository.navigator.pop()
    }

    fun resetErrorValues() {
        _state.value = _state.value.copy(
            isNameError = false,
            isDescriptionError = false,
            nameSupportingText = "",
            descriptionSupportingText = ""
        )
    }

    private fun basicValuesCheck(name: String, description: String) {
        if (name.isEmpty()) {
            _state.value = _state.value.copy(isNameError = true, nameSupportingText = localization.petUploadFormNameEmptyError)
        }
        if (name.isNotEmpty() && name.length < MINIMUM_NAME_CHARS) {
            _state.value = _state.value.copy(isNameError = true, nameSupportingText = localization.petUploadFormNameLengthError)
        }
        if (description.isEmpty()) {
            _state.value = _state.value.copy(isDescriptionError = true, descriptionSupportingText = localization.petUploadFormDescriptionEmptyError)
        }
        if (description.isNotEmpty() && description.length < MINIMUM_DESCRIPTION_CHARS) {
            _state.value = _state.value.copy(isDescriptionError = true, descriptionSupportingText = localization.petUploadFormDescriptionLengthError)
        }
    }
}

data class PetUploadState(
    val name: String,
    val description: String,
    val images: List<String>,
    val category: PetCategory,
    val location: GeoLocation,
    val breed: String,
    val color: String,
    val age: PetAge,
    val gender: PetGender,
    val size: PetSize,
    val status: PetStatus,
    val isNameError: Boolean,
    val nameSupportingText: String,
    val isDescriptionError: Boolean,
    val descriptionSupportingText: String
)

sealed class PetUploadSideEffects {
    data object Initial : PetUploadSideEffects()
    data object OnSubmitted : PetUploadSideEffects()
    data class OnSubmittedError(val errorMessage: String) : PetUploadSideEffects()
}
