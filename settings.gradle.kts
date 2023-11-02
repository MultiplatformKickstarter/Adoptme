pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }

    versionCatalogs {
        create("libs")
    }
}

rootProject.name = "My Project Name"
include(":androidApp")
include(":shared")
include(":desktopApp")
//include(":webApp")
include(":backend")
