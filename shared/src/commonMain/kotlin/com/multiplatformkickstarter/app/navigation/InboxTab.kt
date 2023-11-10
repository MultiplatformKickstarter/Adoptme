package com.multiplatformkickstarter.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.screens.ProTemplateFeature

internal object InboxTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(MultiplatformKickstarterIcons.Inbox)

            return remember {
                TabOptions(
                    index = 3u,
                    title = getCurrentLocalization().inbox,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ProTemplateFeature()
    }
}
