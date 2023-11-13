package com.multiplatformkickstarter.app.feature.pets.di

import cafe.adriel.voyager.navigator.Navigator
import com.multiplatformkickstarter.app.feature.pets.viewmodels.MyPetsViewModel
import org.koin.dsl.module

val petsModule = module {
    factory { (navigator: Navigator) ->
        MyPetsViewModel(navigator)
    }
}
