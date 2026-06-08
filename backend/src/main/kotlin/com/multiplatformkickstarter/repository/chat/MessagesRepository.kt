package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.models.MessageResponse

interface MessagesRepository {
    suspend fun sendMessage(conversationId: Int, senderUserId: Int, content: String): MessageResponse?
    suspend fun getMessages(conversationId: Int): List<MessageResponse>
}
