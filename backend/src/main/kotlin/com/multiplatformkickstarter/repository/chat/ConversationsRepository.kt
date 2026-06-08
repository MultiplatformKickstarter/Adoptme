package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.models.ConversationResponse

interface ConversationsRepository {
    suspend fun createConversation(petId: Int, petName: String, buyerUserId: Int, sellerUserId: Int): ConversationResponse?
    suspend fun getConversationsForUser(userId: Int): List<ConversationResponse>
    suspend fun getConversation(conversationId: Int): ConversationResponse?
    suspend fun deleteConversation(conversationId: Int, userId: Int): Boolean
}
