package com.multiplatformkickstarter.app.feature.inbox

import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.common.model.ChatMessage
import com.multiplatformkickstarter.app.feature.inbox.repositories.FakeMockedChatRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ChatRepositoryMockTest {

    private fun makeRepo() = FakeMockedChatRepository()

    @Test
    fun `getConversations returns non-empty list`() = runTest {
        assertTrue(makeRepo().getConversations().isNotEmpty())
    }

    @Test
    fun `getConversations returns two default conversations`() = runTest {
        assertEquals(2, makeRepo().getConversations().size)
    }

    @Test
    fun `getMessages returns seeded messages for conversation 1`() = runTest {
        assertTrue(makeRepo().getMessages(conversationId = 1).isNotEmpty())
    }

    @Test
    fun `getMessages returns empty list for unknown conversation`() = runTest {
        assertTrue(makeRepo().getMessages(conversationId = 999).isEmpty())
    }

    @Test
    fun `sendMessage adds sent message and auto-reply`() = runTest {
        val repo = makeRepo()
        val before = repo.getMessages(1).size
        repo.sendMessage(conversationId = 1, content = "Hi!")
        assertEquals(before + 2, repo.getMessages(1).size)
    }

    @Test
    fun `sendMessage returns message with correct content`() = runTest {
        val sent = makeRepo().sendMessage(conversationId = 1, content = "Test message")
        assertNotNull(sent)
        assertEquals("Test message", sent.content)
    }

    @Test
    fun `auto-reply comes from a different sender than sent message`() = runTest {
        val repo = makeRepo()
        val beforeSize = repo.getMessages(1).size
        repo.sendMessage(conversationId = 1, content = "Hello")
        val messages = repo.getMessages(1)
        val sent = messages[beforeSize]
        val reply = messages[beforeSize + 1]
        assertTrue(sent.senderUserId != reply.senderUserId)
    }

    @Test
    fun `deleteConversation returns true`() = runTest {
        assertTrue(makeRepo().deleteConversation(1))
    }

    @Test
    fun `createConversation returns conversation with correct petId`() = runTest {
        val conv = makeRepo().createConversation(petId = 1, petName = "Buddy", sellerUserId = 2)
        assertNotNull(conv)
        assertEquals(1, conv.petId)
    }

    @Test
    fun `multiple sends accumulate messages`() = runTest {
        val repo = makeRepo()
        val before = repo.getMessages(1).size
        repo.sendMessage(1, "first")
        repo.sendMessage(1, "second")
        assertEquals(before + 4, repo.getMessages(1).size)
    }
}
