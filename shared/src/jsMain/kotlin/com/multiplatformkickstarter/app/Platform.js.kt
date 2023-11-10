package com.multiplatformkickstarter.app

class JsPlatform : Platform {
    override val name: String = "JS"
}

actual fun getPlatform(): Platform = JsPlatform()