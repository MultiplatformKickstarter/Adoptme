package com.multiplatformkickstarter.app.di

import androidx.compose.material3.SnackbarHostState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.data.repositories.LastSearchAdsMockRepository
import com.multiplatformkickstarter.app.data.repositories.NearMeAdsMockRepository
import com.multiplatformkickstarter.app.data.repositories.ProfileRepository
import com.multiplatformkickstarter.app.data.repositories.SessionRepository
import com.multiplatformkickstarter.app.data.usecases.GetLastSearchUseCase
import com.multiplatformkickstarter.app.data.usecases.GetNearMeAdsUseCase
import com.multiplatformkickstarter.app.data.usecases.GetSearchUseCase
import com.multiplatformkickstarter.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.multiplatformkickstarter.app.feature.debugmenu.viewmodel.DebugMenuViewModel
import com.multiplatformkickstarter.app.feature.petupload.repositories.PetUploadPublishRepository
import com.multiplatformkickstarter.app.feature.petupload.usecases.PetUploadUseCase
import com.multiplatformkickstarter.app.feature.petupload.viewmodel.PetUploadViewModel
import com.multiplatformkickstarter.app.feature.profile.viewmodels.ProfileDetailViewModel
import com.multiplatformkickstarter.app.feature.search.repositories.LastSearchesRepository
import com.multiplatformkickstarter.app.feature.search.repositories.PetsFromSearchRepository
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.platform.MultiplatformKickstarterEventTracker
import com.multiplatformkickstarter.app.platform.RootNavigatorRepository
import com.multiplatformkickstarter.app.platform.RootSnackbarHostStateRepository
import com.multiplatformkickstarter.app.platform.ServiceClient
import com.multiplatformkickstarter.app.ui.screens.viewmodel.HomeScreenViewModel
import com.multiplatformkickstarter.app.ui.screens.viewmodel.ProfileViewModel
import com.multiplatformkickstarter.app.ui.screens.viewmodel.SearchListingViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    factory { (navigator: Navigator) ->
        HomeScreenViewModel(navigator, get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    factory {
        PetUploadViewModel(get(), get(), get(), get())
    }
    factory { (navigator: Navigator) ->
        ProfileViewModel(navigator, get(), get(), get())
    }

    factory { (userId: Int, navigator: Navigator) ->
        ProfileDetailViewModel(userId, navigator, get(), get())
    }

    factory { (searchId: Int?, petCategory: PetCategory?, navigator: Navigator) ->
        SearchListingViewModel(searchId, petCategory, get(), navigator)
    }

    factory {
        DebugMenuViewModel(get())
    }

    single { (navigator: Navigator, tabNavigator: TabNavigator) ->
        RootNavigatorRepository(navigator, tabNavigator)
    }

    single { (snackbarHostState: SnackbarHostState) ->
        RootSnackbarHostStateRepository(snackbarHostState)
    }

    single {
        MultiplatformKickstarterEventTracker()
    }

    factoryOf(::GlobalAppSettingsRepository)
    factoryOf(::GetSearchUseCase)
    singleOf(::NearMeAdsMockRepository)
    factoryOf(::GetNearMeAdsUseCase)

    singleOf(::LastSearchesRepository)
    singleOf(::LastSearchAdsMockRepository)
    singleOf(::PetsFromSearchRepository)
    factoryOf(::GetLastSearchUseCase)

    singleOf(::SessionRepository)
    singleOf(::ProfileRepository)
    singleOf(::Settings)
    single { ServiceClient }
    single { getCurrentLocalization() }

    factoryOf(::PetUploadUseCase)
    singleOf(::PetUploadPublishRepository)
}
