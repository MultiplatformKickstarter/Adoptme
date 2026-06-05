package com.multiplatformkickstarter.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import com.multiplatformkickstarter.app.ui.screens.MainScreen
import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageBitmapDecoder

@Suppress("FunctionName")
@Composable
fun MainApp() {
    val kamelConfig = remember {
        KamelConfig {
            takeFrom(KamelConfig.Core)
            imageBitmapDecoder()
        }
    }
    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        Navigator(MainScreen()) {
            ScaleTransition(it)
        }
    }
}
