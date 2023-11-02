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
/*
    js(IR) {
        browser()
    }
*/
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
                // implementation("org.jetbrains.compose.ui:ui-test-junit4-uikitarm64:1.4.3")
                // implementation(compose.preview)
                implementation(compose.materialIconsExtended)
                // implementation(compose.uiTooling)
                /*@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTestJUnit4)*/
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
/*
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation("io.ktor:ktor-client-js:2.2.1")
                implementation("io.ktor:ktor-client-json-js:2.1.0")
                dependsOn(commonMain)
            }
        }
*/
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
    namespace = "com.myprojectname.app"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
