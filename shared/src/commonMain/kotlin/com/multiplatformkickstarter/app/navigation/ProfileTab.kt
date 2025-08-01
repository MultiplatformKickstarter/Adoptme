package com.multiplatformkickstarter.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.ScaleTransition
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.screens.ProfileTabScreen

internal object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(MultiplatformKickstarterIcons.Person)

            return remember {
                TabOptions(
                    index = 4u,
                    title = getCurrentLocalization().profile,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(ProfileTabScreen()) {
            ScaleTransition(it)
        }
    }
}
