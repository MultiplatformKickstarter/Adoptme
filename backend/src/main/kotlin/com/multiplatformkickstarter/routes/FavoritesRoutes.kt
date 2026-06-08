package com.multiplatformkickstarter.routes

import com.multiplatformkickstarter.API_VERSION
import com.multiplatformkickstarter.auth.JWT_CONFIGURATION
import com.multiplatformkickstarter.auth.UserSession
import com.multiplatformkickstarter.repository.favorites.FavoritesRepository
import com.multiplatformkickstarter.repository.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions

const val FAVORITES = "$API_VERSION/favorites"
const val FAVORITES_ADD = "$FAVORITES/add"
const val FAVORITES_REMOVE = "$FAVORITES/remove"

@Resource(FAVORITES)
class FavoritesListRoute

@Resource(FAVORITES_ADD)
class FavoritesAddRoute

@Resource(FAVORITES_REMOVE)
class FavoritesRemoveRoute

fun Route.favorites(
    favoritesRepository: FavoritesRepository,
    userRepository: UserRepository,
) {
    authenticate(JWT_CONFIGURATION) {
        get<FavoritesListRoute> {
            val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
                return@get
            }
            val petIds = favoritesRepository.getFavorites(user.userId)
            call.respond(HttpStatusCode.OK, petIds)
        }

        post<FavoritesAddRoute> {
            val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
                return@post
            }
            val params = call.receive<Parameters>()
            val petId = params["petId"]?.toIntOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing petId")
            val added = favoritesRepository.addFavorite(user.userId, petId)
            call.respond(if (added) HttpStatusCode.Created else HttpStatusCode.Conflict)
        }

        delete<FavoritesRemoveRoute> {
            val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
                return@delete
            }
            val params = call.receive<Parameters>()
            val petId = params["petId"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing petId")
            val removed = favoritesRepository.removeFavorite(user.userId, petId)
            call.respond(if (removed) HttpStatusCode.OK else HttpStatusCode.NotFound)
        }
    }
}
