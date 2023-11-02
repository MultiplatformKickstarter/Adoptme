package com.myprojectname.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.ui.icon.MyProjectNameIcons
import com.myprojectname.app.ui.screens.EmptyComingSoon

internal object FavoritesTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(MyProjectNameIcons.Favorite)

            return remember {
                TabOptions(
                    index = 1u,
                    title = getCurrentLocalization().favorites,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        EmptyComingSoon()
    }
}
