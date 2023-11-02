package com.myprojectname.routes

import com.myprojectname.API_VERSION
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.auth.JWT_CONFIGURATION
import com.myprojectname.auth.UserSession
import com.myprojectname.repository.pets.PetsRepository
import com.myprojectname.repository.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location
import io.ktor.server.locations.delete
import io.ktor.server.locations.get
import io.ktor.server.locations.patch
import io.ktor.server.locations.post
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val PETS = "$API_VERSION/pets"
const val PETS_CREATE = "$PETS/create"
const val PETS_LIST = "$PETS/list"
const val PETS_GET_ONE = "$PETS/pet"
const val PETS_UPDATE_ONE = "$PETS/pet/update"
const val PETS_DELETE = "$PETS/delete"

@KtorExperimentalLocationsAPI
@Location(PETS_CREATE)
class PetsCreateRoute

@KtorExperimentalLocationsAPI
@Location(PETS_LIST)
class PetsListRoute

@KtorExperimentalLocationsAPI
@Location(PETS_GET_ONE)
class PetsGetOneRoute

@KtorExperimentalLocationsAPI
@Location(PETS_UPDATE_ONE)
class PetsUpdateOneRoute

@KtorExperimentalLocationsAPI
@Location(PETS_DELETE)
class PetsDeleteRoute

@Suppress("LongMethod", "TooGenericExceptionCaught", "CyclomaticComplexMethod")
@KtorExperimentalLocationsAPI
fun Route.pets(petsRepository: PetsRepository, userRepository: UserRepository) {
    authenticate(JWT_CONFIGURATION) {
        post<PetsCreateRoute> {
            val petsParameters = call.receive<Parameters>()
            val title = petsParameters["title"] ?: ""
            val description = petsParameters["description"] ?: ""
            val images = petsParameters["images"] ?: ""
            val category = PetCategory.entries[petsParameters["category"]?.toInt() ?: 0].id
            val location = petsParameters["location"] ?: ""
            val breed = petsParameters["breed"] ?: ""
            val age = petsParameters["age"] ?: ""
            val gender = petsParameters["gender"] ?: ""
            val size = petsParameters["size"] ?: ""
            val color = petsParameters["color"] ?: ""
            val status = petsParameters["status"] ?: ""
            val shelterId = petsParameters["shelterId"]?.toInt() ?: -1

            val user = call.sessions.get<UserSession>()?.let {
                userRepository.findUser(it.userId)
            }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@post
            }

            val date = Calendar.getInstance().time
            val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()).format(date)

            try {
                val currentPet = petsRepository.addPet(
                    userId = user.userId,
                    title = title,
                    description = description,
                    images = images,
                    category = category,
                    location = location,
                    published = formattedDate,
                    breed = breed,
                    age = age,
                    gender = gender,
                    size = size,
                    color = color,
                    status = status,
                    shelterId = shelterId
                )
                currentPet?.id?.let {
                    call.respond(HttpStatusCode.OK, currentPet)
                }
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to add Pet", e)
                call.respond(HttpStatusCode.BadRequest, "Problems Uploading Pet")
            }
        }

        get<PetsGetOneRoute> {
            val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            val petsParameters = call.receive<Parameters>()
            val petId = petsParameters["id"]?.toInt() ?: -1

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@get
            }
            try {
                val pet = petsRepository.getPet(petId)
                call.respond(pet)
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to get Pet", e)
                call.respond(HttpStatusCode.BadRequest, "Problems getting Pet")
            }
        }

        get<PetsListRoute> {
            val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@get
            }
            try {
                val pets = petsRepository.getPets(user.userId)
                call.respond(pets)
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to get Pets", e)
                call.respond(HttpStatusCode.BadRequest, "Problems getting Pets")
            }
        }

        patch<PetsUpdateOneRoute> {
            val petsParameters = call.receive<Parameters>()
            val petId = petsParameters["id"]?.toInt()
            val title = petsParameters["title"] ?: ""
            val description = petsParameters["description"] ?: ""
            val images = petsParameters["images"] ?: ""
            val location = petsParameters["location"] ?: ""
            val breed = petsParameters["breed"] ?: ""
            val age = petsParameters["age"] ?: ""
            val gender = petsParameters["gender"] ?: ""
            val size = petsParameters["size"] ?: ""
            val color = petsParameters["color"] ?: ""
            val status = petsParameters["status"] ?: ""
            val shelterId = petsParameters["shelterId"]?.toInt() ?: -1

            val user = call.sessions.get<UserSession>()?.let {
                userRepository.findUser(it.userId)
            }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@patch
            }

            val date = Calendar.getInstance().time
            val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()).format(date)

            try {
                if (petId != null) {
                    val currentPet = petsRepository.updatePet(
                        petId = petId.toInt(),
                        title = title,
                        description = description,
                        images = images,
                        location = location,
                        modified = formattedDate,
                        breed = breed,
                        age = age,
                        gender = gender,
                        size = size,
                        color = color,
                        status = status,
                        shelterId = shelterId
                    )
                    currentPet?.id?.let {
                        call.respond(HttpStatusCode.OK, currentPet)
                        return@patch
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound, "Pet not found")
                    return@patch
                }
            } catch (e: Throwable) {
                this@authenticate.application.log.error("Failed to update Pet", e)
                call.respond(HttpStatusCode.BadRequest, "Problems updating Pet")
            }
        }

        delete<PetsDeleteRoute> {
            val petsParameters = call.receive<Parameters>()
            val petId = petsParameters["id"]?.toInt()

            val user = call.sessions.get<UserSession>()?.let {
                userRepository.findUser(it.userId)
            }

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                return@delete
            }

            if (petId != null) {
                petsRepository.delete(petId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Problems deleting Pet")
                return@delete
            }
        }
    }
}
