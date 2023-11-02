package com.myprojectname.app.feature.debugmenu.repositories

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.data.repositories.ProfileRepository
import com.myprojectname.app.data.repositories.SessionRepository
import com.myprojectname.app.localization.AvailableLanguages
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.platform.Environment
import com.myprojectname.app.platform.ServerEnvironment
import com.russhwolf.settings.Settings

const val ENVIRONMENT_KEY = "ENVIRONMENT_KEY"
const val LANGUAGE_KEY = "LANGUAGE_KEY"
const val MOCKED_CONTENT_KEY = "MOCKED_CONTENT_KEY"
const val MOCKED_USER_KEY = "MOCKED_USER_KEY"

class GlobalAppSettingsRepository(
    private val settings: Settings,
    private val sessionRepository: SessionRepository,
    private val profileRepository: ProfileRepository,
    private val localization: Localization
) {
    fun getCurrentEnvironment(): Environment {
        return when (settings.getString(ENVIRONMENT_KEY, ServerEnvironment.PRODUCTION.name)) {
            ServerEnvironment.PRODUCTION.name -> {
                ServerEnvironment.PRODUCTION
            }
            ServerEnvironment.PREPRODUCTION.name -> {
                ServerEnvironment.PREPRODUCTION
            }
            else -> {
                ServerEnvironment.LOCALHOST
            }
        }
    }

    fun getCurrentLanguage(): AvailableLanguages {
        return when (settings.getString(LANGUAGE_KEY, AvailableLanguages.EN.name)) {
            AvailableLanguages.EN.name -> AvailableLanguages.EN
            AvailableLanguages.ES.name -> AvailableLanguages.ES
            AvailableLanguages.FR.name -> AvailableLanguages.FR
            AvailableLanguages.IT.name -> AvailableLanguages.IT
            AvailableLanguages.DE.name -> AvailableLanguages.DE
            else -> {
                AvailableLanguages.EN
            }
        }
    }

    fun isMockedContentEnabled(): Boolean {
        return settings.getBoolean(MOCKED_CONTENT_KEY, false)
    }

    fun setMockedContentCheckStatus(checked: Boolean) {
        settings.putBoolean(MOCKED_CONTENT_KEY, checked)
    }

    fun setSelectedEnvironment(environment: Environment) {
        settings.putString(ENVIRONMENT_KEY, environment.name)
    }

    fun setSelectedLanguage(language: AvailableLanguages) {
        settings.putString(LANGUAGE_KEY, language.name)
    }

    fun isMockedUserEnabled(): Boolean {
        return settings.getBoolean(MOCKED_USER_KEY, false)
    }

    fun setMockedUserCheckStatus(checked: Boolean) {
        settings.putBoolean(MOCKED_USER_KEY, checked)

        if (checked) {
            initMockedUser()
        } else {
            sessionRepository.clear()
        }
    }

    private fun initMockedUser() {
        sessionRepository.initSession(
            id = -1,
            email = "john@doe.com",
            session = "session",
            token = "token"
        )
        profileRepository.initProfile(
            userId = -1,
            name = "John Doe",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi quam purus, auctor non aliquet at, lacinia ut metus. Nam laoreet felis et pharetra elementum.",
            image = "https://images.unsplash.com/photo-1682977192828-3c59809146f8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80",
            geoLocation = GeoLocation(41.403785, 2.175651),
            rating = 4.3
        )
    }
}
