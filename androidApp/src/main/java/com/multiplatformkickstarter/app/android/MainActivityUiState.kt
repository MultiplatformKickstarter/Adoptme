package com.multiplatformkickstarter.app.android

import com.multiplatformkickstarter.app.common.model.UserData

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
