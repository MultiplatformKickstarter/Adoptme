package com.multiplatformkickstarter.app.feature.debugmenu

class AndroidDebug : Debug {
    override val isDebug: Boolean = false
}

actual fun getDebug(): Debug = AndroidDebug()
