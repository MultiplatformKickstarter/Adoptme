package com.multiplatformkickstarter.app.feature.inbox.repositories

import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.common.model.ChatMessage

private val autoReplies = listOf(
    "That sounds great!",
    "Sure, when would you like to meet?",
    "He/She is very friendly and loves cuddles.",
    "The adoption fee is already included.",
    "Yes, all vaccinations are up to date!",
    "Feel free to come by this weekend.",
    "I'll send you more photos shortly.",
    "We'd love for them to find a good home!",
    "Let me know if you have any other questions.",
    "They get along well with other pets too.",
)

class FakeMockedChatRepository {
    private val conversations = mutableListOf(
        ChatConversation(id = 1, petId = 1, petName = "Buddy", buyerUserId = -1, sellerUserId = 2, createdAt = 0L),
        ChatConversation(id = 2, petId = 2, petName = "Luna", buyerUserId = 3, sellerUserId = -1, createdAt = 0L),
    )
    private val messageStore: MutableMap<Int, MutableList<ChatMessage>> = mutableMapOf(
        1 to mutableListOf(
            ChatMessage(id = 1, conversationId = 1, senderUserId = 2, content = "Is Buddy available?", sentAt = 0L),
            ChatMessage(id = 2, conversationId = 1, senderUserId = -1, content = "Yes, come meet him!", sentAt = 0L),
        ),
        2 to mutableListOf(
            ChatMessage(id = 3, conversationId = 2, senderUserId = -1, content = "Interested in Luna.", sentAt = 0L),
            ChatMessage(id = 4, conversationId = 2, senderUserId = 3, content = "She is very calm.", sentAt = 0L),
        ),
    )
    private var nextId = 100

    suspend fun getConversations(): List<ChatConversation> = conversations.toList()

    suspend fun createConversation(petId: Int, petName: String, sellerUserId: Int): ChatConversation? {
        return conversations.firstOrNull { it.petId == petId }
            ?: ChatConversation(id = nextId++, petId = petId, petName = petName, buyerUserId = -1, sellerUserId = sellerUserId, createdAt = 0L)
                .also { conversations.add(it) }
    }

    suspend fun deleteConversation(conversationId: Int): Boolean = true

    suspend fun getMessages(conversationId: Int): List<ChatMessage> =
        messageStore.getOrElse(conversationId) { mutableListOf() }.toList()

    suspend fun sendMessage(conversationId: Int, content: String): ChatMessage {
        val store = messageStore.getOrPut(conversationId) { mutableListOf() }
        val sent = ChatMessage(id = nextId++, conversationId = conversationId, senderUserId = -1, content = content, sentAt = 0L)
        store.add(sent)
        val reply = ChatMessage(id = nextId++, conversationId = conversationId, senderUserId = 2, content = autoReplies[store.size % autoReplies.size], sentAt = 0L)
        store.add(reply)
        return sent
    }
}
