package com.multiplatformkickstarter.app.localization

import androidx.compose.runtime.Composable

actual fun getCurrentLanguage(): AvailableLanguages = AvailableLanguages.EN

@Suppress("FunctionName")
@Composable
actual fun SetLanguage(language: AvailableLanguages) {
}
