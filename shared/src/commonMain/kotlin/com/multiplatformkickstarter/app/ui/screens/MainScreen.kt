package com.multiplatformkickstarter.app.ui.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.multiplatformkickstarter.app.feature.petupload.PetUploadScreen
import com.multiplatformkickstarter.app.navigation.FavoritesTab
import com.multiplatformkickstarter.app.navigation.HomeTab
import com.multiplatformkickstarter.app.navigation.InboxTab
import com.multiplatformkickstarter.app.navigation.PetUploadTab
import com.multiplatformkickstarter.app.navigation.ProfileTab
import com.multiplatformkickstarter.app.platform.RootNavigatorRepository
import com.multiplatformkickstarter.app.platform.RootSnackbarHostStateRepository
import com.multiplatformkickstarter.app.ui.components.MultiplatformKickstarterNavigationBar
import com.multiplatformkickstarter.app.ui.components.MultiplatformKickstarterNavigationBarItem
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import org.koin.core.parameter.ParametersHolder
import org.koin.mp.KoinPlatform

class MainScreen : Screen {
    @Composable
    override fun Content() {
        MultiplatformKickstarterTheme {
            MainScreenView()
        }
    }
}

@Composable
fun MainScreenView() {
    val snackbarHostState = remember { SnackbarHostState() }
    val rootNavigator = LocalNavigator.currentOrThrow
    TabNavigator(
        HomeTab,
        disposeNestedNavigators = false
    ) { _ ->
        val rootNavigatorRepository = setupRootNavigator(rootNavigator, LocalTabNavigator.current)
        val rootSnackbarHostStateRepository = setupRootSnackbarHostState(snackbarHostState)

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(rootSnackbarHostStateRepository.snackbarHostState) },
            bottomBar = {
                MultiplatformKickstarterNavigationBar {
                    TabNavigationItem(HomeTab, rootNavigatorRepository)
                    TabNavigationItem(FavoritesTab, rootNavigatorRepository)
                    TabNavigationItem(PetUploadTab, rootNavigatorRepository)
                    TabNavigationItem(InboxTab, rootNavigatorRepository)
                    TabNavigationItem(ProfileTab, rootNavigatorRepository)
                }
            }
        ) {
            SlideTransition(LocalNavigator.currentOrThrow) { screen ->
                screen.Content()
            }
        }
    }
}

fun setupRootNavigator(rootNavigator: Navigator, tabNavigator: TabNavigator): RootNavigatorRepository {
    val koin = KoinPlatform.getKoin()
    return koin.get(null, parameters = { ParametersHolder(listOf(rootNavigator, tabNavigator).toMutableList(), false) })
}

fun setupRootSnackbarHostState(snackbarHostState: SnackbarHostState): RootSnackbarHostStateRepository {
    val koin = KoinPlatform.getKoin()
    return koin.get(null, parameters = { ParametersHolder(listOf(snackbarHostState).toMutableList(), false) })
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab, rootNavigator: RootNavigatorRepository) {
    val tabNavigator = LocalTabNavigator.current
    val currentDestination = tabNavigator.current.key == tab.key

    MultiplatformKickstarterNavigationBarItem(
        selected = currentDestination,
        onClick = {
            if (tab is PetUploadTab) {
                rootNavigator.navigator.push(PetUploadScreen())
            } else {
                tabNavigator.current = tab
            }
        },
        icon = {
            tab.options.icon?.let {
                androidx.compose.material3.Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        },
        label = { androidx.compose.material3.Text(tab.options.title) }
    )
}
