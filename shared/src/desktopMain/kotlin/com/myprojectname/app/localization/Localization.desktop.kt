package com.myprojectname.app.localization

import androidx.compose.runtime.Composable

actual fun getCurrentLanguage(): AvailableLanguages = AvailableLanguages.EN

@Composable
actual fun SetLanguage(language: AvailableLanguages) {
}
