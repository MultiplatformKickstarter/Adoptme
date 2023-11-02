package com.myprojectname.app.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int,
    val userId: Int,
    val name: String,
    val description: String?,
    val image: String?,
    val location: GeoLocation?,
    val rating: Double?
)
