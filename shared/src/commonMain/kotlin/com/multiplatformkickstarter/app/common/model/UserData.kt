package com.multiplatformkickstarter.app.common.model

import com.multiplatformkickstarter.app.common.model.theme.DarkThemeConfig
import com.multiplatformkickstarter.app.common.model.theme.ThemeBrand

data class UserData(
    val bookmarkedNewsResources: Set<String>,
    val followedTopics: Set<String>,
    val followedAuthors: Set<String>,
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
)
