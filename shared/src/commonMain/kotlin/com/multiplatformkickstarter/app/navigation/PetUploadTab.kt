package com.multiplatformkickstarter.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.ScaleTransition
import com.multiplatformkickstarter.app.feature.petupload.PetUploadScreen
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons

internal object PetUploadTab : Tab {
    private val localization = getCurrentLocalization()

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(MultiplatformKickstarterIcons.Create)

            return remember {
                TabOptions(
                    index = 2u,
                    title = localization.upload,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(PetUploadScreen()) {
            ScaleTransition(it)
        }
    }
}
