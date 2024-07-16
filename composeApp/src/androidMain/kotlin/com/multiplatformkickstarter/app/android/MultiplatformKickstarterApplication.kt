package com.multiplatformkickstarter.app.android

import android.app.Application
import com.multiplatformkickstarter.app.android.core.di.DependencyContainer

class MultiplatformKickstarterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDependencyContainer()
    }

    private fun initDependencyContainer() {
        DependencyContainer.initialize(this)
    }
}
