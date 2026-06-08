package com.multiplatformkickstarter.app.feature.favorites.repositories

import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.data.repositories.LastSearchAdsMockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository(
    private val lastSearchAdsMockRepository: LastSearchAdsMockRepository,
) {
    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    fun isFavorite(petId: Int): Boolean = _favoriteIds.value.contains(petId)

    fun toggleFavorite(petId: Int) {
        val current = _favoriteIds.value
        _favoriteIds.value = if (current.contains(petId)) {
            current - petId
        } else {
            current + petId
        }
    }

    fun getFavoritePets(): List<PetModel> {
        val allPets = lastSearchAdsMockRepository.getAds().getOrNull() ?: emptyList()
        return allPets.filter { _favoriteIds.value.contains(it.id) }
    }
}
