package com.multiplatformkickstarter.app.di

import com.multiplatformkickstarter.app.feature.loginsignup.di.logInSignUpModule
import com.multiplatformkickstarter.app.feature.pets.di.petsModule

fun appModule() = listOf(
    commonModule,
    logInSignUpModule,
    petsModule
)
