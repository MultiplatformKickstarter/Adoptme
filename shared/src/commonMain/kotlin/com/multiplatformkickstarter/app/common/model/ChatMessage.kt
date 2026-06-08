package com.multiplatformkickstarter.app.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: Int,
    val conversationId: Int,
    val senderUserId: Int,
    val content: String,
    val sentAt: Long,
)
