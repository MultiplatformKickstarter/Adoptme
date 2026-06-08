package com.multiplatformkickstarter.app.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatConversation(
    val id: Int,
    val petId: Int,
    val petName: String,
    val buyerUserId: Int,
    val sellerUserId: Int,
    val createdAt: Long,
)
