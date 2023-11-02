package com.myprojectname.app.android

import android.app.Application
import com.myprojectname.app.android.core.di.DependencyContainer

class MyProjectNameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDependencyContainer()
    }

    private fun initDependencyContainer() {
        DependencyContainer.initialize(this)
    }
}
