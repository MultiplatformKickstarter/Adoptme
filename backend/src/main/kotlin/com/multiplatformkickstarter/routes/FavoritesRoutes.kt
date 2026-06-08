package com.multiplatformkickstarter.routes

import com.multiplatformkickstarter.API_VERSION
import com.multiplatformkickstarter.auth.JWT_CONFIGURATION
import com.multiplatformkickstarter.models.DatabaseUser
import com.multiplatformkickstarter.repository.favorites.FavoritesRepository
import com.multiplatformkickstarter.repository.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

const val FAVORITES = "$API_VERSION/favorites"
const val FAVORITES_ADD = "$FAVORITES/add"
const val FAVORITES_REMOVE = "$FAVORITES/remove"

@Resource(FAVORITES)
class FavoritesListRoute

@Resource(FAVORITES_ADD)
class FavoritesAddRoute

@Resource(FAVORITES_REMOVE)
class FavoritesRemoveRoute

@Suppress("UnusedParameter")
fun Route.favorites(
    favoritesRepository: FavoritesRepository,
    userRepository: UserRepository,
) {
    authenticate(JWT_CONFIGURATION) {
        get<FavoritesListRoute> {
            val user = call.principal<DatabaseUser>()
                ?: return@get call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
            val petIds = favoritesRepository.getFavorites(user.userId)
            call.respond(HttpStatusCode.OK, petIds)
        }

        post<FavoritesAddRoute> {
            val user = call.principal<DatabaseUser>()
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
            val params = call.receive<Parameters>()
            val petId = params["petId"]?.toIntOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing petId")
            val added = favoritesRepository.addFavorite(user.userId, petId)
            call.respond(if (added) HttpStatusCode.Created else HttpStatusCode.Conflict)
        }

        delete<FavoritesRemoveRoute> {
            val user = call.principal<DatabaseUser>()
                ?: return@delete call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
            val petId = call.request.queryParameters["petId"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing petId")
            val removed = favoritesRepository.removeFavorite(user.userId, petId)
            call.respond(if (removed) HttpStatusCode.OK else HttpStatusCode.NotFound)
        }
    }
}
