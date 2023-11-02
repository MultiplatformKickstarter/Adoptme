package com.myprojectname.app.data.repositories

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.Profile
import com.myprojectname.app.extensions.requestAndCatch
import com.myprojectname.app.platform.ServerEnvironment
import com.myprojectname.app.platform.ServiceClient
import com.russhwolf.settings.Settings
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

const val PROFILE_ID_KEY = "PROFILE_ID_KEY"
const val PROFILE_USER_ID_KEY = "PROFILE_USER_ID_KEY"
const val PROFILE_NAME_KEY = "PROFILE_NAME_KEY"
const val PROFILE_DESCRIPTION_KEY = "PROFILE_DESCRIPTION_KEY"
const val PROFILE_RATING_KEY = "PROFILE_RATING_KEY"
const val PROFILE_IMAGE_KEY = "PROFILE_IMAGE_KEY"
const val PROFILE_LOCATION_KEY = "PROFILE_LOCATION_KEY"
const val PROFILE_PATH = "v1/users/profile"

class ProfileRepository(
    private val service: ServiceClient,
    private val settings: Settings
) {
    suspend fun getProfile(userId: Int): Profile {
        return service.httpClient.requestAndCatch(
            {
                this.get("${ServerEnvironment.PRODUCTION.url}/$PROFILE_PATH/$userId").body<Profile>()
            },
            {
                when (response.status) {
                    // TODO: Return correct errors
                    HttpStatusCode.BadRequest -> { throw this }
                    HttpStatusCode.Conflict -> { throw this }
                    else -> throw this
                }
            }
        )
    }

    fun getProfile(): Profile {
        return Profile(
            getId(),
            getUserId(),
            getName(),
            getDescription(),
            getImage(),
            getLocation(),
            getRating()
        )
    }

    private fun getId(): Int {
        return settings.getInt(PROFILE_ID_KEY, -1)
    }

    private fun getUserId(): Int {
        return settings.getInt(PROFILE_USER_ID_KEY, -1)
    }

    fun getName(): String {
        return settings.getString(PROFILE_NAME_KEY, "")
    }

    fun getDescription(): String {
        return settings.getString(PROFILE_DESCRIPTION_KEY, "")
    }

    fun getImage(): String {
        return settings.getString(PROFILE_IMAGE_KEY, "")
    }

    fun getLocation(): GeoLocation? {
        val locationDeserialized = settings.getString(PROFILE_LOCATION_KEY, "").split(",")
        return if (locationDeserialized.size == 2) {
            try {
                GeoLocation(locationDeserialized[0].toDouble(), locationDeserialized[1].toDouble())
            } catch (_: Throwable) {
                // TODO log error
                null
            }
        } else {
            null
        }
    }

    fun getRating(): Double {
        return settings.getDouble(PROFILE_RATING_KEY, 0.0)
    }

    fun initProfile(userId: Int, name: String, description: String?, image: String?, geoLocation: GeoLocation?, rating: Double?) {
        settings.putInt(PROFILE_ID_KEY, userId)
        settings.putString(PROFILE_NAME_KEY, name)
        description?.let {
            settings.putString(PROFILE_DESCRIPTION_KEY, it)
        }
        image?.let {
            settings.putString(PROFILE_IMAGE_KEY, it)
        }
        geoLocation?.let {
            val locationSerialized = "${it.latitude},${it.longitude}"
            settings.putString(PROFILE_LOCATION_KEY, locationSerialized)
        }
        rating?.let {
            settings.putDouble(PROFILE_RATING_KEY, it)
        }
    }
}
