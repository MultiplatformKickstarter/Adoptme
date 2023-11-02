package com.myprojectname.app.di

import androidx.compose.material3.SnackbarHostState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.data.repositories.LastSearchAdsMockRepository
import com.myprojectname.app.data.repositories.NearMeAdsMockRepository
import com.myprojectname.app.data.repositories.ProfileRepository
import com.myprojectname.app.data.repositories.SessionRepository
import com.myprojectname.app.data.usecases.GetLastSearchUseCase
import com.myprojectname.app.data.usecases.GetNearMeAdsUseCase
import com.myprojectname.app.data.usecases.GetSearchUseCase
import com.myprojectname.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.myprojectname.app.feature.debugmenu.viewmodel.DebugMenuViewModel
import com.myprojectname.app.feature.petupload.repositories.PetUploadPublishRepository
import com.myprojectname.app.feature.petupload.usecases.PetUploadUseCase
import com.myprojectname.app.feature.petupload.viewmodel.PetUploadViewModel
import com.myprojectname.app.feature.profile.viewmodels.ProfileDetailViewModel
import com.myprojectname.app.feature.search.repositories.LastSearchesRepository
import com.myprojectname.app.feature.search.repositories.PetsFromSearchRepository
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.platform.MyProjectNameEventTracker
import com.myprojectname.app.platform.RootNavigatorRepository
import com.myprojectname.app.platform.RootSnackbarHostStateRepository
import com.myprojectname.app.platform.ServiceClient
import com.myprojectname.app.ui.screens.viewmodel.HomeScreenViewModel
import com.myprojectname.app.ui.screens.viewmodel.ProfileViewModel
import com.myprojectname.app.ui.screens.viewmodel.SearchListingViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    factory { (navigator: Navigator) ->
        HomeScreenViewModel(navigator, get(), get(), get(), get(), get(), get(), get(), get())
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
        MyProjectNameEventTracker()
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
