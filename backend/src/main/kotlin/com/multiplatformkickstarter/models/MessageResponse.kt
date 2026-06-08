package com.multiplatformkickstarter.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: Int,
    val conversationId: Int,
    val senderUserId: Int,
    val content: String,
    val sentAt: Long,
)
