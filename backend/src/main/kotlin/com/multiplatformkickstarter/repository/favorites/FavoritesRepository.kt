package com.multiplatformkickstarter.repository.favorites

interface FavoritesRepository {
    suspend fun addFavorite(userId: Int, petId: Int): Boolean
    suspend fun removeFavorite(userId: Int, petId: Int): Boolean
    suspend fun getFavorites(userId: Int): List<Int>
    suspend fun isFavorite(userId: Int, petId: Int): Boolean
}
