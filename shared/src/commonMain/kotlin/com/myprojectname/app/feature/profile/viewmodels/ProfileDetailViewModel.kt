package com.myprojectname.app.feature.profile.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.data.repositories.ProfileRepository
import com.myprojectname.app.data.repositories.SessionRepository
import com.myprojectname.app.ui.screens.PetDetailScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileDetailViewModel(
    private val userId: Int,
    private var navigator: Navigator,
    private val profileRepository: ProfileRepository,
    private val sessionRepository: SessionRepository
) : ScreenModel {
    private val _state = MutableStateFlow(ProfileDetailState("", "", "", null, 0.0, emptyList()))
    val state = _state.asStateFlow()
    private val _sideEffects = Channel<ProfileDetailSideEffects>()
    val sideEffects: Flow<ProfileDetailSideEffects> = _sideEffects.receiveAsFlow()

    fun onStarted() {
        screenModelScope.launch {
            val profile = if (userId == sessionRepository.getUserId()) {
                profileRepository.getProfile()
            } else {
                profileRepository.getProfile(userId)
            }
            val pets = emptyList<PetModel>()
            _state.value = ProfileDetailState(
                name = profile.name,
                description = profile.description,
                image = profile.image,
                location = profile.location,
                rating = profile.rating,
                pets = pets
            )
        }
    }

    fun onPetDetailClicked(id: Int) {
        navigator.push(PetDetailScreen(id))
    }
}

data class ProfileDetailState(
    val name: String,
    val description: String?,
    val image: String?,
    val location: GeoLocation?,
    val rating: Double?,
    val pets: List<PetModel>
)

sealed class ProfileDetailSideEffects {
    data object Initial : ProfileDetailSideEffects()
}
