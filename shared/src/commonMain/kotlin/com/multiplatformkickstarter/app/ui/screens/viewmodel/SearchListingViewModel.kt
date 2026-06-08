package com.multiplatformkickstarter.app.ui.screens.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import co.touchlab.kermit.Logger
import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.data.usecases.GetSearchUseCase
import com.multiplatformkickstarter.app.feature.favorites.repositories.FavoritesRepository
import com.multiplatformkickstarter.app.ui.screens.PetDetailScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchListingViewModel(
    private val searchId: Int?,
    private val petCategory: PetCategory?,
    private val searchListingUserCase: GetSearchUseCase,
    private val navigator: Navigator,
    private val favoritesRepository: FavoritesRepository,
) : ScreenModel {
    private val _state = MutableStateFlow(SearchListingState(null, null, emptyList(), emptySet()))
    val state = _state.asStateFlow()
    private val _sideEffects = Channel<SearchListingSideEffects>()
    val sideEffects: Flow<SearchListingSideEffects> = _sideEffects.receiveAsFlow()

    init {
        favoritesRepository.favoriteIds
            .onEach { _state.value = _state.value.copy(favoriteIds = it) }
            .launchIn(screenModelScope)
    }

    fun onStarted() {
        screenModelScope.launch {
            if (searchId != null) {
                searchListingUserCase.invoke(searchId)
                    .onSuccess {
                        _state.value = _state.value.copy(pets = it)
                        _sideEffects.trySend(SearchListingSideEffects.OnLoaded(it))
                    }
                    .onFailure {
                        _sideEffects.trySend(SearchListingSideEffects.OnLoadError)
                        Logger.w(it) { "Error on loading search" }
                    }
            } else if (petCategory != null) {
                searchListingUserCase.invoke(petCategory)
                    .onSuccess {
                        _state.value = _state.value.copy(pets = it)
                        _sideEffects.trySend(SearchListingSideEffects.OnLoaded(it))
                    }
                    .onFailure {
                        _sideEffects.trySend(SearchListingSideEffects.OnLoadError)
                        Logger.w(it) { "Error on loading search" }
                    }
            } else {
                _sideEffects.trySend(SearchListingSideEffects.OnNoResults)
            }
        }
    }

    fun onPetClicked(id: Int) {
        navigator.push(PetDetailScreen(id))
    }

    fun onFavoriteToggled(petId: Int) {
        favoritesRepository.toggleFavorite(petId)
    }
}

data class SearchListingState(
    val searchId: Int? = null,
    val petCategory: PetCategory? = null,
    val pets: List<PetModel>,
    val favoriteIds: Set<Int>,
)

sealed class SearchListingSideEffects {
    data object Initial : SearchListingSideEffects()

    data class OnLoaded(val pets: List<PetModel>) : SearchListingSideEffects()

    data object OnNoResults : SearchListingSideEffects()

    data object OnLoadError : SearchListingSideEffects()
}
