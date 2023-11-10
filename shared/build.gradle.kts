plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.skiko)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.animation)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.ktor.client)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.server.serialization.kotlinx.json)

                // Kamel for image loading
                implementation(libs.kamel)

                // Voyager for Navigation
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.bottom.sheet.navigator)
                implementation(libs.voyager.tab.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)

                // Multiplatform Settings to encrypted key-value data
                implementation(libs.multiplatform.settings.no.arg)
                implementation(libs.multiplatform.settings.serialization)

                // Dependency Injection
                implementation(libs.koin.core)
                implementation(libs.koin.test)

                // Logging
                implementation(libs.kermit)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                dependsOn(commonMain)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.multiplatform.settings.test)
            }
        }
    }
}

android {
    namespace = "com.multiplatformkickstarter.app"
    compileSdk = properties["multiplatformkickstarter.android.compileSdk"].toString().toInt()
    defaultConfig {
        minSdk = properties["multiplatformkickstarter.android.minSdk"].toString().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
