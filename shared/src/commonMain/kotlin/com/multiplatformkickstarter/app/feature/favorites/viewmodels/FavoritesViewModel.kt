package com.multiplatformkickstarter.app.feature.favorites.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.feature.favorites.repositories.FavoritesRepository
import com.multiplatformkickstarter.app.ui.screens.PetDetailScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavoritesViewModel(
    private var navigator: Navigator,
    private val favoritesRepository: FavoritesRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(FavoritesState(favorites = emptyList()))
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        favoritesRepository.favoriteIds
            .onEach { _state.value = _state.value.copy(favorites = favoritesRepository.getFavoritePets()) }
            .launchIn(screenModelScope)
    }

    fun onStarted(navigator: Navigator) {
        this.navigator = navigator
        _state.value = _state.value.copy(favorites = favoritesRepository.getFavoritePets())
    }

    fun onPetDetailClicked(petId: Int) {
        navigator.push(PetDetailScreen(petId))
    }

    fun onUnfavoriteClicked(petId: Int) {
        favoritesRepository.toggleFavorite(petId)
    }
}

data class FavoritesState(
    val favorites: List<PetModel>,
)
