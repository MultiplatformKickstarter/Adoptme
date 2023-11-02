package com.myprojectname.app.feature.loginsignup.di

import com.myprojectname.app.data.repositories.AuthenticationRepository
import com.myprojectname.app.data.usecases.LoadProfileUseCase
import com.myprojectname.app.feature.loginsignup.usecases.EmailLogInUseCase
import com.myprojectname.app.feature.loginsignup.usecases.EmailSignUpUseCase
import com.myprojectname.app.feature.loginsignup.viewmodels.EmailLoginViewModel
import com.myprojectname.app.feature.loginsignup.viewmodels.EmailSignUpViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val logInSignUpModule = module {
    factory {
        EmailLoginViewModel(get(), get(), get(), get(), get())
    }
    factory {
        EmailSignUpViewModel(get(), get(), get(), get(), get())
    }

    factoryOf(::EmailLogInUseCase)
    factoryOf(::EmailSignUpUseCase)
    factory { LoadProfileUseCase(get(), get()) }
    factory { AuthenticationRepository(get()) }
}
