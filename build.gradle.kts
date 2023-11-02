plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    kotlin("android").version(libs.versions.kotlin).apply(false)
    kotlin("multiplatform").version(libs.versions.kotlin).apply(false)
    kotlin("plugin.serialization").version(libs.versions.kotlin).apply(false)
    alias(libs.plugins.compose.multiplatform).apply(false)
    alias(libs.plugins.ktlint).apply(false)
    alias(libs.plugins.detekt).apply(false)
}

subprojects {
    if (name != "desktopApp") {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        apply(plugin = "io.gitlab.arturbosch.detekt")

        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            debug.set(true)
        }

        configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            parallel = false
            config.setFrom("../config/detekt-config.yml")
            buildUponDefaultConfig = false
        }

    }
}