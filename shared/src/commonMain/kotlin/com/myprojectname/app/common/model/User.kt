package com.myprojectname.app.common.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String
)

data class AuthenticationResponse(
    val id: Int,
    val session: String,
    val token: String
)
