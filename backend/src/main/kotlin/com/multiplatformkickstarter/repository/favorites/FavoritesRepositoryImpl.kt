package com.multiplatformkickstarter.repository.favorites

import com.multiplatformkickstarter.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select

class FavoritesRepositoryImpl : FavoritesRepository {

    override suspend fun addFavorite(userId: Int, petId: Int): Boolean {
        if (isFavorite(userId, petId)) return false
        dbQuery {
            Favorites.insert {
                it[Favorites.userId] = userId
                it[Favorites.petId] = petId
            }
        }
        return true
    }

    override suspend fun removeFavorite(userId: Int, petId: Int): Boolean {
        val deleted = dbQuery {
            Favorites.deleteWhere {
                (Favorites.userId eq userId) and (Favorites.petId eq petId)
            }
        }
        return deleted > 0
    }

    override suspend fun getFavorites(userId: Int): List<Int> {
        return dbQuery {
            Favorites.select(Favorites.petId)
                .where { Favorites.userId eq userId }
                .map { it[Favorites.petId] }
        }
    }

    override suspend fun isFavorite(userId: Int, petId: Int): Boolean {
        return dbQuery {
            Favorites.select(Favorites.id)
                .where { (Favorites.userId eq userId) and (Favorites.petId eq petId) }
                .count() > 0
        }
    }
}
