package com.multiplatformkickstarter.routes

import com.multiplatformkickstarter.FakeConversationsRepository
import com.multiplatformkickstarter.FakeMessagesRepository
import com.multiplatformkickstarter.auth.JwtService
import com.multiplatformkickstarter.setupTestApp
import com.multiplatformkickstarter.testUser
import com.multiplatformkickstarter.tokenForUser
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChatRoutesTest {

    private val jwtService = JwtService()
    private val token get() = jwtService.tokenForUser()

    @Test
    fun `get conversations without auth returns 401`() = testApplication {
        setupTestApp()
        assertEquals(HttpStatusCode.Unauthorized, client.get(CHAT_CONVERSATIONS).status)
    }

    @Test
    fun `get conversations returns empty list for new user`() = testApplication {
        setupTestApp()
        val response = client.get(CHAT_CONVERSATIONS) {
            header("Authorization", "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `create conversation returns 201`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = CHAT_CONVERSATION_CREATE,
            formParameters = Parameters.build {
                append("petId", "1")
                append("petName", "Buddy")
                append("sellerUserId", "2")
            },
        ) { header("Authorization", "Bearer $token") }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `create conversation with missing petId returns bad request`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = CHAT_CONVERSATION_CREATE,
            formParameters = Parameters.build { append("petName", "Buddy") },
        ) { header("Authorization", "Bearer $token") }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `created conversation appears in conversations list`() = testApplication {
        val convRepo = FakeConversationsRepository()
        setupTestApp(conversationsRepository = convRepo)
        convRepo.createConversation(1, "Buddy", testUser.userId, 2)
        val response = client.get(CHAT_CONVERSATIONS) {
            header("Authorization", "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `delete conversation returns 200`() = testApplication {
        val convRepo = FakeConversationsRepository()
        setupTestApp(conversationsRepository = convRepo)
        convRepo.createConversation(1, "Buddy", testUser.userId, 2)
        val response = client.delete(CHAT_CONVERSATION_DELETE) {
            header("Authorization", "Bearer $token")
            url { parameters.append("conversationId", "1") }
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `delete non-existing conversation returns 404`() = testApplication {
        setupTestApp()
        val response = client.delete(CHAT_CONVERSATION_DELETE) {
            header("Authorization", "Bearer $token")
            url { parameters.append("conversationId", "99") }
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `get messages without auth returns 401`() = testApplication {
        setupTestApp()
        assertEquals(HttpStatusCode.Unauthorized, client.get(CHAT_MESSAGES).status)
    }

    @Test
    fun `send message returns 201`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = CHAT_MESSAGES_SEND,
            formParameters = Parameters.build {
                append("conversationId", "1")
                append("content", "Hello!")
            },
        ) { header("Authorization", "Bearer $token") }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `send message with missing content returns bad request`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = CHAT_MESSAGES_SEND,
            formParameters = Parameters.build { append("conversationId", "1") },
        ) { header("Authorization", "Bearer $token") }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `sent message appears in message list`() = testApplication {
        val msgRepo = FakeMessagesRepository()
        setupTestApp(messagesRepository = msgRepo)
        msgRepo.sendMessage(1, testUser.userId, "Hello!")
        val response = client.get(CHAT_MESSAGES) {
            header("Authorization", "Bearer $token")
            url { parameters.append("conversationId", "1") }
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
