package com.myprojectname.app.feature.pets.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.feature.pets.MyPetsScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MyPetsViewModel(
    private val navigator: Navigator
) : ScreenModel {
    private val _state = MutableStateFlow(MyPetsState(emptyList()))
    val state = _state.asStateFlow()
    private val _sideEffects = Channel<MyPetsSideEffects>()
    val sideEffects: Flow<MyPetsSideEffects> = _sideEffects.receiveAsFlow()

    fun invoke() {
        screenModelScope.launch {
        }
    }

    fun onMyPetsClicked() {
        navigator.push(MyPetsScreen())
    }
}

data class MyPetsState(
    val ads: List<PetModel>
)

sealed class MyPetsSideEffects {
    data object Initial : MyPetsSideEffects()
}
