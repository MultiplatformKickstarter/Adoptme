package com.multiplatformkickstarter.app.feature.inbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.components.EmptyLayout
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import com.multiplatformkickstarter.app.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
class InboxScreen : Screen {

    @Composable
    override fun Content() {
        val localization = getCurrentLocalization()

        MultiplatformKickstarterTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = localization.inbox,
                                style = Typography.get().headlineSmall,
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                    )
                },
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                    EmptyLayout(
                        title = localization.inboxEmptyTitle,
                        description = localization.inboxEmptyDescription,
                        localization = localization,
                    ) {}
                }
            }
        }
    }
}
