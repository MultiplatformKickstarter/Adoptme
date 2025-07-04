@file:Suppress("ktlint:standard:filename")

package com.multiplatformkickstarter.app

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun homeScreenViewController(): UIViewController =
    ComposeUIViewController {
        MainApp()
    }
