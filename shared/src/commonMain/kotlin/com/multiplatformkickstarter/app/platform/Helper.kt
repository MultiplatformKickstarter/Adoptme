package com.multiplatformkickstarter.app.platform

import com.multiplatformkickstarter.app.di.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule())
    }
}
