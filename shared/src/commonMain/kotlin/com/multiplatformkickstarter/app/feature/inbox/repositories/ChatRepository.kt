package com.multiplatformkickstarter.app.feature.inbox.repositories

import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.common.model.ChatMessage
import com.multiplatformkickstarter.app.data.repositories.SessionRepository
import com.multiplatformkickstarter.app.feature.debugmenu.repositories.GlobalAppSettingsRepository
import com.multiplatformkickstarter.app.platform.ServerEnvironment
import com.multiplatformkickstarter.app.platform.ServiceClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters

private const val CONVERSATIONS_PATH = "v1/chat/conversations"
private const val CONVERSATION_CREATE_PATH = "v1/chat/conversations/create"
private const val CONVERSATION_DELETE_PATH = "v1/chat/conversations/delete"
private const val MESSAGES_PATH = "v1/chat/messages"
private const val MESSAGES_SEND_PATH = "v1/chat/messages/send"

private val mockConversations = listOf(
    ChatConversation(id = 1, petId = 1, petName = "Buddy", buyerUserId = -1, sellerUserId = 2, createdAt = 1717776000000L),
    ChatConversation(id = 2, petId = 2, petName = "Luna", buyerUserId = 3, sellerUserId = -1, createdAt = 1717862400000L),
)

private val mockAutoReplies = listOf(
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

private val mockMessageStore: MutableMap<Int, MutableList<ChatMessage>> = mutableMapOf(
    1 to mutableListOf(
        ChatMessage(id = 1, conversationId = 1, senderUserId = 2, content = "Hi! Is Buddy still available?", sentAt = 1717776000000L),
        ChatMessage(id = 2, conversationId = 1, senderUserId = -1, content = "Yes, come meet him anytime!", sentAt = 1717779600000L),
    ),
    2 to mutableListOf(
        ChatMessage(id = 3, conversationId = 2, senderUserId = -1, content = "I'm interested in Luna.", sentAt = 1717862400000L),
        ChatMessage(id = 4, conversationId = 2, senderUserId = 3, content = "She's wonderful, very calm.", sentAt = 1717866000000L),
    ),
)
private var mockNextId = 100

class ChatRepository(
    private val service: ServiceClient,
    private val sessionRepository: SessionRepository,
    private val globalAppSettingsRepository: GlobalAppSettingsRepository,
) {

    private val baseUrl get() = ServerEnvironment.PRODUCTION.url
    private val authToken get() = sessionRepository.getToken()

    suspend fun getConversations(): List<ChatConversation> {
        if (globalAppSettingsRepository.isMockedContentEnabled()) return mockConversations
        return try {
            val response = service.httpClient.get("$baseUrl/$CONVERSATIONS_PATH") {
                header("Authorization", "Bearer $authToken")
            }
            when {
                response.status.value in 200..299 -> response.body()
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createConversation(petId: Int, petName: String, sellerUserId: Int): ChatConversation? {
        if (globalAppSettingsRepository.isMockedContentEnabled()) {
            return mockConversations.firstOrNull { it.petId == petId }
                ?: ChatConversation(id = petId, petId = petId, petName = petName, buyerUserId = -1, sellerUserId = sellerUserId, createdAt = 0L)
        }
        return try {
            val response = service.httpClient.submitForm(
                url = "$baseUrl/$CONVERSATION_CREATE_PATH",
                formParameters = Parameters.build {
                    append("petId", petId.toString())
                    append("petName", petName)
                    append("sellerUserId", sellerUserId.toString())
                }
            ) {
                header("Authorization", "Bearer $authToken")
            }
            when {
                response.status.value in 200..299 -> response.body()
                response.status == HttpStatusCode.Conflict -> getConversations().firstOrNull { it.petId == petId }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteConversation(conversationId: Int): Boolean {
        if (globalAppSettingsRepository.isMockedContentEnabled()) return true
        return try {
            val response = service.httpClient.delete("$baseUrl/$CONVERSATION_DELETE_PATH") {
                header("Authorization", "Bearer $authToken")
                url { parameters.append("conversationId", conversationId.toString()) }
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getMessages(conversationId: Int): List<ChatMessage> {
        if (globalAppSettingsRepository.isMockedContentEnabled()) {
            return mockMessageStore.getOrPut(conversationId) { mutableListOf() }.toList()
        }
        return try {
            val response = service.httpClient.get("$baseUrl/$MESSAGES_PATH") {
                header("Authorization", "Bearer $authToken")
                url { parameters.append("conversationId", conversationId.toString()) }
            }
            when {
                response.status.value in 200..299 -> response.body()
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun sendMessage(conversationId: Int, content: String): ChatMessage? {
        if (globalAppSettingsRepository.isMockedContentEnabled()) {
            val store = mockMessageStore.getOrPut(conversationId) { mutableListOf() }
            val sent = ChatMessage(id = mockNextId++, conversationId = conversationId, senderUserId = -1, content = content, sentAt = 0L)
            store.add(sent)
            val reply = ChatMessage(id = mockNextId++, conversationId = conversationId, senderUserId = 2, content = mockAutoReplies[(store.size) % mockAutoReplies.size], sentAt = 0L)
            store.add(reply)
            return sent
        }
        return try {
            val response = service.httpClient.submitForm(
                url = "$baseUrl/$MESSAGES_SEND_PATH",
                formParameters = Parameters.build {
                    append("conversationId", conversationId.toString())
                    append("content", content)
                }
            ) {
                header("Authorization", "Bearer $authToken")
            }
            when {
                response.status.value in 200..299 -> response.body()
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
}
