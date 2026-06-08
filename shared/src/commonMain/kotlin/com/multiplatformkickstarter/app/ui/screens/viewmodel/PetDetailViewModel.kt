package com.multiplatformkickstarter.app.ui.screens.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.data.repositories.LastSearchAdsMockRepository
import com.multiplatformkickstarter.app.feature.favorites.repositories.FavoritesRepository
import com.multiplatformkickstarter.app.feature.inbox.ChatScreen
import com.multiplatformkickstarter.app.feature.inbox.repositories.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PetDetailViewModel(
    private val petId: Int,
    private val navigator: Navigator,
    private val lastSearchAdsMockRepository: LastSearchAdsMockRepository,
    private val favoritesRepository: FavoritesRepository,
    private val chatRepository: ChatRepository,
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

    fun onAdoptClicked() {
        val pet = _state.value.pet ?: return
        screenModelScope.launch {
            val conversation = chatRepository.createConversation(
                petId = pet.id,
                petName = pet.title,
                sellerUserId = pet.userId,
            )
            if (conversation != null) {
                navigator.push(ChatScreen(conversation))
            }
        }
    }
}

data class PetDetailState(
    val pet: PetModel?,
    val isFavorite: Boolean,
)
