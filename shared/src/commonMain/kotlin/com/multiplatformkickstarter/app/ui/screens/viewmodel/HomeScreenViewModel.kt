package com.multiplatformkickstarter.app.ui.screens.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.common.model.SearchModel
import com.multiplatformkickstarter.app.data.repositories.ProfileRepository
import com.multiplatformkickstarter.app.data.repositories.SessionRepository
import com.multiplatformkickstarter.app.data.usecases.GetLastSearchUseCase
import com.multiplatformkickstarter.app.data.usecases.GetNearMeAdsUseCase
import com.multiplatformkickstarter.app.feature.debugmenu.DebugMenuScreen
import com.multiplatformkickstarter.app.feature.debugmenu.getDebug
import com.multiplatformkickstarter.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.multiplatformkickstarter.app.feature.loginsignup.LoginSignUpLandingScreen
import com.multiplatformkickstarter.app.feature.petupload.PetUploadScreen
import com.multiplatformkickstarter.app.feature.search.repositories.LastSearchesRepository
import com.multiplatformkickstarter.app.localization.AvailableLanguages
import com.multiplatformkickstarter.app.localization.Localization
import com.multiplatformkickstarter.app.platform.RootNavigatorRepository
import com.multiplatformkickstarter.app.ui.screens.OnboardingScreen
import com.multiplatformkickstarter.app.ui.screens.PetDetailScreen
import com.multiplatformkickstarter.app.ui.screens.SearchListingScreen
import com.russhwolf.settings.Settings
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val ONBOARDING_VIEWED_KEY = "onboarding_viewed_key"

class HomeScreenViewModel(
    private var navigator: Navigator,
    private val getNearMeAdsUseCase: GetNearMeAdsUseCase,
    private val getLastSearchUseCase: GetLastSearchUseCase,
    private val sessionRepository: SessionRepository,
    private val profileRepository: ProfileRepository,
    private val localization: Localization,
    private val rootNavigatorRepository: RootNavigatorRepository,
    private val lastSearchesRepository: LastSearchesRepository,
    private val globalAppSettingsRepository: GlobalAppSettingsRepository,
    private val settings: Settings
) : ScreenModel {
    private val _sideEffects = Channel<HomeScreenSideEffects>()
    val sideEffects: Flow<HomeScreenSideEffects> = _sideEffects.receiveAsFlow()
    private val _state = MutableStateFlow(
        HomeScreenState(
            userName = null,
            greeting = localization.homeScreenSignUpLogin,
            header = getMainHeader(null),
            nearMeAds = emptyList(),
            lastSearchAds = emptyList(),
            currentLanguage = AvailableLanguages.EN
        )
    )
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
    private val debugComponent = getDebug()

    init {
        if (!debugComponent.isDebug) {
            if (!sessionRepository.isLoggedIn()) {
                navigator.push(LoginSignUpLandingScreen())
            }
        }

        if (!settings.getBoolean(ONBOARDING_VIEWED_KEY, false)) {
            settings.putBoolean(ONBOARDING_VIEWED_KEY, true)
            rootNavigatorRepository.navigator.push(OnboardingScreen())
        }
    }

    private fun getGreeting(): String {
        return if (sessionRepository.isLoggedIn()) {
            val currentTime = Clock.System.now()
            val timeZone = TimeZone.currentSystemDefault()
            val localDateTime = currentTime.toLocalDateTime(timeZone)

            when (localDateTime.hour) {
                in 6..11 -> localization.greetingMorning
                in 12..17 -> localization.greetingAfternoon
                in 16..21 -> localization.greetingEvening
                else -> localization.greetingNight
            }
        } else {
            localization.homeScreenSignUpLogin
        }
    }

    private fun getMainHeader(userName: String?): String {
        return if (!userName.isNullOrEmpty()) userName else localization.welcome
    }

    fun onViewSearchClicked(searchId: Int) {
        navigator.push(SearchListingScreen(searchId = searchId))
    }

    fun onPetDetailClicked(petId: Int) {
        navigator.push(PetDetailScreen(petId))
    }

    fun onSignUpLoginClicked() {
        if (!sessionRepository.isLoggedIn()) {
            rootNavigatorRepository.navigator.push(LoginSignUpLandingScreen())
        }
    }

    fun onCategoryClicked(petCategory: PetCategory) {
        lastSearchesRepository.add(SearchModel(petCategory = petCategory))
        navigator.push(SearchListingScreen(petCategory = petCategory))
    }

    fun onStarted(navigator: Navigator) {
        this.navigator = navigator

        screenModelScope.launch {
            getLastSearchUseCase.invoke()
                .onSuccess {
                    _state.value = _state.value.copy(lastSearchAds = it)
                }
            getNearMeAdsUseCase.invoke()
                .onSuccess {
                    _state.value = _state.value.copy(nearMeAds = it)
                }
            _state.value = _state.value.copy(
                userName = profileRepository.getName(),
                greeting = getGreeting(),
                header = getMainHeader(profileRepository.getName()),
                nearMeAds = _state.value.nearMeAds,
                lastSearchAds = _state.value.lastSearchAds,
                currentLanguage = globalAppSettingsRepository.getCurrentLanguage()
            )
        }
    }

    fun onDebugMenuClicked() {
        rootNavigatorRepository.navigator.push(DebugMenuScreen())
    }

    fun onUploadPetClicked() {
        rootNavigatorRepository.navigator.push(PetUploadScreen())
    }
}

data class HomeScreenState(
    val userName: String?,
    val greeting: String,
    val header: String,
    val nearMeAds: List<PetModel>,
    val lastSearchAds: List<PetModel>,
    val currentLanguage: AvailableLanguages
)

sealed class HomeScreenSideEffects
