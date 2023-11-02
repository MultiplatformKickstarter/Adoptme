package com.myprojectname.app.di

import com.myprojectname.app.feature.loginsignup.di.logInSignUpModule
import com.myprojectname.app.feature.pets.di.petsModule

fun appModule() = listOf(
    commonModule,
    logInSignUpModule,
    petsModule
)
