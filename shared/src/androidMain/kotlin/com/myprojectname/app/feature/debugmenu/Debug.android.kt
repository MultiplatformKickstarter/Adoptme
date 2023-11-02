package com.myprojectname.app.feature.debugmenu

import co.touchlab.kermit.BuildConfig

class AndroidDebug : Debug {
    override val isDebug: Boolean = BuildConfig.DEBUG
}

actual fun getDebug(): Debug = AndroidDebug()
