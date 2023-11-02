package com.myprojectname.routes

import com.myprojectname.API_VERSION
import com.myprojectname.auth.JWT_CONFIGURATION
import com.myprojectname.auth.UserSession
import com.myprojectname.repository.profile.ProfileRepository
import com.myprojectname.repository.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.locations.patch
import io.ktor.server.locations.post
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions

const val PROFILE = "$API_VERSION/profile"
const val PROFILE_CREATE = "$PROFILE/create"
const val PROFILE_UPDATE = "$PROFILE/update"

@KtorExperimentalLocationsAPI
@Location(PROFILE)
class ProfileRoute

@KtorExperimentalLocationsAPI
@Location(PROFILE_CREATE)
class ProfileCreateRoute

@KtorExperimentalLocationsAPI
@Location(PROFILE_UPDATE)
class ProfileUpdateRoute

@Suppress("LongMethod", "TooGenericExceptionCaught", "CyclomaticComplexMethod")
@KtorExperimentalLocationsAPI
fun Route.profiles(
    profileRepository: ProfileRepository,
    userRepository: UserRepository
) {
    authenticate(JWT_CONFIGURATION) {
        post<ProfileCreateRoute> {
            val profileParameters = call.receive<Parameters>()
            val name = profileParameters["name"] ?: ""
            val description = profileParameters["description"] ?: ""
            val image = profileParameters["image"] ?: ""
            val location = profileParameters["location"] ?: ""
            val rating = profileParameters["rating"] ?: ""

            val user = call.sessions.get<UserSession>()?.let {
                userRepository.findUser(it.userId)
            }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@post
            }

            try {
                val profile = profileRepository.addProfile(
                    user.userId,
                    name = name,
                    description = description,
                    image = image,
                    location = location,
                    rating = rating.toDouble()
                )
                profile?.id?.let {
                    call.respond(HttpStatusCode.OK, profile)
                }
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to add Profile", e)
                call.respond(HttpStatusCode.BadRequest, "Problems Adding Profile")
            }
        }

        get<ProfileRoute> {
            val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
                return@get
            }
            try {
                val profile = profileRepository.getProfile(user.userId)
                if (profile != null) {
                    call.respond(profile)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Profile not found")
                    return@get
                }
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to get Profile", e)
                call.respond(HttpStatusCode.BadRequest, "Problems getting Profile")
            }
        }

        patch<ProfileUpdateRoute> {
            val profileParameters = call.receive<Parameters>()
            val name = profileParameters["name"] ?: ""
            val description = profileParameters["description"] ?: ""
            val image = profileParameters["image"] ?: ""
            val location = profileParameters["location"] ?: ""
            val rating = profileParameters["rating"] ?: ""

            val user = call.sessions.get<UserSession>()?.let {
                userRepository.findUser(it.userId)
            }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@patch
            }

            try {
                val profile = profileRepository.updateProfile(
                    userId = user.userId,
                    name = name,
                    description = description,
                    image = image,
                    location = location,
                    rating = rating.toDouble()
                )
                profile?.id?.let {
                    call.respond(HttpStatusCode.OK, profile)
                }
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to update Profile", e)
                call.respond(HttpStatusCode.BadRequest, "Problems updating Profile")
            }
        }
    }
}
