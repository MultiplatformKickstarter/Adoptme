package com.multiplatformkickstarter.app.feature.loginsignup.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.multiplatformkickstarter.app.data.repositories.UnauthorizedException
import com.multiplatformkickstarter.app.data.usecases.LoadProfileUseCase
import com.multiplatformkickstarter.app.feature.loginsignup.usecases.EmailSignUpUseCase
import com.multiplatformkickstarter.app.localization.Localization
import com.multiplatformkickstarter.app.platform.MultiplatformKickstarterEventTracker
import com.multiplatformkickstarter.app.platform.RootNavigatorRepository
import com.multiplatformkickstarter.app.platform.TrackEvents
import com.multiplatformkickstarter.app.platform.isValidEmail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val MINIMUM_PASSWORD_CHARS = 8

class EmailSignUpViewModel(
    private val emailSignUpUseCase: EmailSignUpUseCase,
    private val loadProfileUseCase: LoadProfileUseCase,
    private val localization: Localization,
    private val rootNavigatorRepository: RootNavigatorRepository,
    private val eventTracker: MultiplatformKickstarterEventTracker
) : ScreenModel {
    private val _state = MutableStateFlow(
        EmailSignUpState(
            name = "",
            email = "",
            password = "",
            emailError = "",
            passwordError = localization.signUpPasswordSupport,
            isEmailError = false,
            isPasswordError = false
        )
    )
    val state = _state.asStateFlow()
    private val _sideEffects = Channel<EmailSignUpSideEffects>()
    val sideEffects: Flow<EmailSignUpSideEffects> = _sideEffects.receiveAsFlow()

    fun onSubmit(name: String, email: String, password: String) {
        resetErrorValues()
        basicValuesCheck(email, password)
        screenModelScope.launch {
            emailSignUpUseCase.invoke(name, email, password)
                .onSuccess {
                    loadProfileUseCase.invoke(it)
                    eventTracker.onEventTracked(TrackEvents.SIGN_UP_SUCCESSFUL)
                    _sideEffects.trySend(EmailSignUpSideEffects.OnSignedUp)
                }
                .onFailure {
                    eventTracker.onEventTracked(TrackEvents.SIGN_UP_ERROR)
                    when (it) {
                        is UnauthorizedException -> _sideEffects.trySend(EmailSignUpSideEffects.OnSignUpError(localization.genericFailedDefaultMessage))
                        else -> _sideEffects.trySend(EmailSignUpSideEffects.OnSignUpError(localization.genericFailedDefaultMessage))
                    }
                    Logger.w(it) { "Error on login" }
                }
        }
    }

    fun onSignedUp() {
        rootNavigatorRepository.navigator.popUntilRoot()
    }

    private fun resetErrorValues() {
        _state.value = _state.value.copy(
            isEmailError = false,
            isPasswordError = false,
            emailError = "",
            passwordError = localization.signUpPasswordSupport
        )
    }

    private fun basicValuesCheck(email: String, password: String) {
        if (email.isEmpty()) {
            _state.value = _state.value.copy(isEmailError = true, emailError = localization.loginEmailEmptyError)
        }
        if (email.isNotEmpty() && !email.isValidEmail()) {
            _state.value = _state.value.copy(isEmailError = true, emailError = localization.loginEmailFormatError)
        }
        if (password.isEmpty()) {
            _state.value = _state.value.copy(isPasswordError = true, passwordError = localization.loginPasswordEmptyError)
        }
        if (password.isNotEmpty() && password.length < MINIMUM_PASSWORD_CHARS) {
            _state.value = _state.value.copy(isPasswordError = true)
        }
    }
}

data class EmailSignUpState(
    val name: String,
    val email: String,
    val password: String,
    val emailError: String,
    val passwordError: String,
    val isEmailError: Boolean,
    val isPasswordError: Boolean
)

sealed class EmailSignUpSideEffects {
    data object Initial : EmailSignUpSideEffects()
    data object OnSignedUp : EmailSignUpSideEffects()
    data class OnSignUpError(val errorMessage: String) : EmailSignUpSideEffects()
}
