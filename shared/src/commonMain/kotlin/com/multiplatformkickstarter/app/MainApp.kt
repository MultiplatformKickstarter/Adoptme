package com.multiplatformkickstarter.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import com.multiplatformkickstarter.app.ui.screens.MainScreen

@Composable
fun MainApp() {
    Navigator(MainScreen()) {
        ScaleTransition(it)
    }
}
