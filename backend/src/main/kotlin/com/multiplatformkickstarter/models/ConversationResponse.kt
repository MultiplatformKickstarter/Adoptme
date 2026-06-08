package com.multiplatformkickstarter.models

import kotlinx.serialization.Serializable

@Serializable
data class ConversationResponse(
    val id: Int,
    val petId: Int,
    val petName: String,
    val buyerUserId: Int,
    val sellerUserId: Int,
    val createdAt: Long,
)
