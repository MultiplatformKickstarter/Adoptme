package com.multiplatformkickstarter.app.feature.debugmenu

class AndroidDebug : Debug {
    override val isDebug: Boolean = true
}

actual fun getDebug(): Debug = AndroidDebug()
