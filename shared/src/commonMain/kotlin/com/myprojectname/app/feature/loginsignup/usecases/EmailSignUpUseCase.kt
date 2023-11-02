package com.myprojectname.app.feature.loginsignup.usecases

import com.myprojectname.app.common.usecases.UseCase
import com.myprojectname.app.data.repositories.AuthenticationRepository
import com.myprojectname.app.data.repositories.SessionRepository

class EmailSignUpUseCase(
    private val authRepository: AuthenticationRepository,
    private val sessionRepository: SessionRepository
) : UseCase() {

    suspend fun invoke(name: String, email: String, password: String): Result<Int> = runCatching {
        val loginResponse = authRepository.signUp(name, email, password)
        sessionRepository.initSession(loginResponse.id, email, loginResponse.session, loginResponse.token)
        return@runCatching loginResponse.id
    }
}
