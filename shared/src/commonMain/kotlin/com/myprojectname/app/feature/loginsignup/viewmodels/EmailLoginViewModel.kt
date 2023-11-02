package com.myprojectname.app.feature.loginsignup.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.myprojectname.app.data.repositories.UnauthorizedException
import com.myprojectname.app.data.usecases.LoadProfileUseCase
import com.myprojectname.app.feature.loginsignup.usecases.EmailLogInUseCase
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.navigation.HomeTab
import com.myprojectname.app.platform.MyProjectNameEventTracker
import com.myprojectname.app.platform.RootNavigatorRepository
import com.myprojectname.app.platform.TrackEvents
import com.myprojectname.app.platform.isValidEmail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EmailLoginViewModel(
    private val emailLogInUseCase: EmailLogInUseCase,
    private val loadProfileUseCase: LoadProfileUseCase,
    private val localization: Localization,
    private val rootNavigatorRepository: RootNavigatorRepository,
    private val eventTracker: MyProjectNameEventTracker
) : ScreenModel {
    private val _state = MutableStateFlow(
        EmailLoginState(
            email = "",
            password = "",
            emailError = "",
            passwordError = "",
            isEmailError = false,
            isPasswordError = false
        )
    )
    val state = _state.asStateFlow()
    private val _sideEffects = Channel<EmailLoginSideEffects>()
    val sideEffects: Flow<EmailLoginSideEffects> = _sideEffects.receiveAsFlow()

    fun onSubmit(email: String, password: String) {
        resetErrorValues()
        basicValuesCheck(email, password)
        if (!_state.value.isEmailError && !_state.value.isPasswordError) {
            screenModelScope.launch {
                emailLogInUseCase.invoke(email, password)
                    .onSuccess {
                        // loadProfileUseCase.invoke(it)
                        eventTracker.onEventTracked(TrackEvents.LOGIN_SUCCESSFUL)
                        _sideEffects.trySend(EmailLoginSideEffects.OnLoggedIn)
                    }
                    .onFailure {
                        eventTracker.onEventTracked(TrackEvents.LOGIN_ERROR)
                        when (it) {
                            is UnauthorizedException -> _sideEffects.trySend(EmailLoginSideEffects.OnLoginError(localization.genericFailedDefaultMessage))
                            else -> _sideEffects.trySend(EmailLoginSideEffects.OnLoginError(localization.genericFailedDefaultMessage))
                        }
                        Logger.w(it) { "Error on login: ${it.message}" }
                    }
            }
        }
    }

    private fun resetErrorValues() {
        _state.value = _state.value.copy(
            isEmailError = false,
            isPasswordError = false,
            emailError = "",
            passwordError = ""
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
    }

    fun onRememberPassword() {
        // TODO
    }

    fun onLoggedIn() {
        rootNavigatorRepository.navigator.popUntilRoot()
        rootNavigatorRepository.tabNavigator.current = HomeTab
    }
}

data class EmailLoginState(
    val email: String,
    val password: String,
    val emailError: String,
    val passwordError: String,
    val isEmailError: Boolean,
    val isPasswordError: Boolean
)

sealed class EmailLoginSideEffects {
    data object Initial : EmailLoginSideEffects()
    data object OnLoggedIn : EmailLoginSideEffects()
    data class OnLoginError(val errorMessage: String) : EmailLoginSideEffects()
}
