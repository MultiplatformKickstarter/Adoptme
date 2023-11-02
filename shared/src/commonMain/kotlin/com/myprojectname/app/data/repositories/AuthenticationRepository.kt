package com.myprojectname.app.data.repositories

import com.myprojectname.app.common.model.AuthenticationResponse
import com.myprojectname.app.extensions.requestAndCatch
import com.myprojectname.app.platform.ServerEnvironment
import com.myprojectname.app.platform.ServiceClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters

private const val LOGIN_PATH = "v1/users/login"
private const val SIGN_UP_PATH = "v1/users/create"
private const val LOGOUT_PATH = "v1/users/logout"

class AuthenticationRepository(private val service: ServiceClient) {

    suspend fun login(email: String, password: String): AuthenticationResponse {
        return service.httpClient.requestAndCatch(
            {
                val response = this.submitForm(
                    url = "${ServerEnvironment.PRODUCTION.url}/$LOGIN_PATH",
                    formParameters = Parameters.build {
                        append("email", email)
                        append("password", password)
                    }
                )
                AuthenticationResponse(
                    id = response.headers["user_id"]?.toInt()!!,
                    session = response.headers["user_session"]!!,
                    token = response.body()
                )
            },
            {
                when (response.status) {
                    HttpStatusCode.Unauthorized -> {
                        throw UnauthorizedException()
                    }
                    HttpStatusCode.BadRequest -> {
                        throw BadRequestException()
                    }
                    else -> throw this
                }
            }
        )
    }

    suspend fun signUp(name: String, email: String, password: String): AuthenticationResponse {
        return service.httpClient.requestAndCatch(
            {
                val response = this.submitForm(
                    url = "${ServerEnvironment.PRODUCTION.url}/$SIGN_UP_PATH",
                    formParameters = Parameters.build {
                        append("name", name)
                        append("email", email)
                        append("password", password)
                    }
                )
                AuthenticationResponse(
                    id = response.headers["user_id"]?.toInt()!!,
                    session = response.headers["user_session"]!!,
                    token = response.body()
                )
            },
            {
                when (response.status) {
                    HttpStatusCode.BadRequest -> {
                        throw BadRequestException()
                    }
                    HttpStatusCode.Conflict -> {
                        throw ConflictException()
                    }
                    else -> throw this
                }
            }
        )
    }

    suspend fun logout() {
        return service.httpClient.requestAndCatch(
            {
                this.post("${ServerEnvironment.PRODUCTION.url}/$LOGOUT_PATH").body()
            },
            {
                when (response.status) {
                    HttpStatusCode.BadRequest -> {
                        throw BadRequestException()
                    }
                    HttpStatusCode.Conflict -> {
                        throw ConflictException()
                    }
                    else -> throw this
                }
            }
        )
    }
}

class UnauthorizedException : Throwable()
class BadRequestException : Throwable()
class ConflictException : Throwable()
