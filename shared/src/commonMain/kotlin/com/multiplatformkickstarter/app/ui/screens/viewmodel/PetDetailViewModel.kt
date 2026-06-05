package com.multiplatformkickstarter.app.ui.screens.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.data.repositories.LastSearchAdsMockRepository
import com.multiplatformkickstarter.app.feature.favorites.repositories.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PetDetailViewModel(
    private val petId: Int,
    private val lastSearchAdsMockRepository: LastSearchAdsMockRepository,
    private val favoritesRepository: FavoritesRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(
        PetDetailState(
            pet = lastSearchAdsMockRepository.getFeaturedAd(petId),
            isFavorite = favoritesRepository.isFavorite(petId),
        )
    )
    val state: StateFlow<PetDetailState> = _state.asStateFlow()

    init {
        favoritesRepository.favoriteIds
            .onEach { _state.value = _state.value.copy(isFavorite = favoritesRepository.isFavorite(petId)) }
            .launchIn(screenModelScope)
    }

    fun onFavoriteToggled() {
        favoritesRepository.toggleFavorite(petId)
    }
}

data class PetDetailState(
    val pet: PetModel?,
    val isFavorite: Boolean,
)
