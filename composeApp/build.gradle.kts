
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

val versionNum: String? by project
val versionMajor = properties["multiplatformkickstarter.version.major"].toString().toInt()
val versionMinor = properties["multiplatformkickstarter.version.minor"].toString().toInt()
val versionPatch = properties["multiplatformkickstarter.version.patch"].toString().toInt()

fun versionCode(): Int {
    versionNum?.let {
        return (versionMajor * 1000000) + (versionMinor * 1000) + it.toInt()
    } ?: return 1
}

kotlin {
    /*@OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }*/
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    jvm("desktop")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    sourceSets {
        val desktopMain by getting
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(project(":shared"))

            // Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.test)
            implementation(libs.koin.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.skiko)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.animation)
            implementation(compose.materialIconsExtended)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.skiko.macos.arm64)

            implementation(libs.androidx.compose.ui.util)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.swingui)
            implementation(libs.ktor.client)
            implementation(libs.ktor.client.java)
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
            implementation(libs.koin.compose)

            // Logging
            implementation(libs.kermit)
        }
    }
}

android {
    namespace = "com.multiplatformkickstarter.app.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.multiplatformkickstarter.app.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = versionCode()
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        implementation(project(":shared"))

        implementation(platform(libs.compose.bom))

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.androidx.tracing.ktx)

        implementation(libs.androidx.ui)
        implementation(libs.androidx.material)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.material3.window.size)
        implementation(libs.androidx.ui.tooling.preview)

        implementation(libs.androidx.activity.compose)
        implementation(libs.accompanist.systemuicontroller)

        implementation(libs.koin.core)
        implementation(libs.koin.android)

        implementation(platform(libs.firebase.bom))
        implementation(libs.firebase.analytics.ktx)

        testImplementation(libs.junit)
        testImplementation(libs.koin.test)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        // androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        debugImplementation(libs.androidx.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.multiplatformkickstarter.app"
            packageVersion = "$versionMajor.$versionMinor.$versionPatch"
        }
    }

    dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.skiko)
        implementation(compose.ui)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(compose.animation)
        implementation(compose.materialIconsExtended)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
        implementation(libs.skiko.macos.arm64)
        implementation(project(":shared"))

        implementation(libs.androidx.compose.ui.util)

        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.swingui)
        implementation(libs.ktor.client)
        implementation(libs.ktor.client.java)
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
        implementation(libs.koin.compose)

        // Logging
        implementation(libs.kermit)
    }
}
