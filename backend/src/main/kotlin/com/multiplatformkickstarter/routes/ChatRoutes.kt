package com.multiplatformkickstarter.routes

import com.multiplatformkickstarter.API_VERSION
import com.multiplatformkickstarter.auth.JWT_CONFIGURATION
import com.multiplatformkickstarter.auth.UserSession
import com.multiplatformkickstarter.repository.chat.ConversationsRepository
import com.multiplatformkickstarter.repository.chat.MessagesRepository
import com.multiplatformkickstarter.repository.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions

const val CHAT = "$API_VERSION/chat"
const val CHAT_CONVERSATIONS = "$CHAT/conversations"
const val CHAT_CONVERSATION_CREATE = "$CHAT_CONVERSATIONS/create"
const val CHAT_CONVERSATION_DELETE = "$CHAT_CONVERSATIONS/delete"
const val CHAT_MESSAGES = "$CHAT/messages"
const val CHAT_MESSAGES_SEND = "$CHAT_MESSAGES/send"

@Resource(CHAT_CONVERSATIONS)
class ChatConversationsRoute

@Resource(CHAT_CONVERSATION_CREATE)
class ChatConversationCreateRoute

@Resource(CHAT_CONVERSATION_DELETE)
class ChatConversationDeleteRoute

@Resource(CHAT_MESSAGES)
class ChatMessagesRoute

@Resource(CHAT_MESSAGES_SEND)
class ChatMessagesSendRoute

fun Route.chat(
    conversationsRepository: ConversationsRepository,
    messagesRepository: MessagesRepository,
    userRepository: UserRepository,
) {
    authenticate(JWT_CONFIGURATION) {
        chatConversationRoutes(conversationsRepository, userRepository)
        chatMessageRoutes(messagesRepository, userRepository)
    }
}

private fun Route.chatConversationRoutes(
    conversationsRepository: ConversationsRepository,
    userRepository: UserRepository,
) {
    get<ChatConversationsRoute> {
        val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            ?: return@get call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
        call.respond(HttpStatusCode.OK, conversationsRepository.getConversationsForUser(user.userId))
    }

    post<ChatConversationCreateRoute> {
        val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            ?: return@post call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
        val params = call.receive<Parameters>()
        val petId = params["petId"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing petId")
        val petName = params["petName"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing petName")
        val sellerUserId = params["sellerUserId"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing sellerUserId")
        val conversation = conversationsRepository.createConversation(petId, petName, user.userId, sellerUserId)
        if (conversation != null) {
            call.respond(HttpStatusCode.Created, conversation)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create conversation")
        }
    }

    delete<ChatConversationDeleteRoute> {
        val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            ?: return@delete call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
        val params = call.receive<Parameters>()
        val conversationId = params["conversationId"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing conversationId")
        val deleted = conversationsRepository.deleteConversation(conversationId, user.userId)
        call.respond(if (deleted) HttpStatusCode.OK else HttpStatusCode.NotFound)
    }
}

private fun Route.chatMessageRoutes(
    messagesRepository: MessagesRepository,
    userRepository: UserRepository,
) {
    get<ChatMessagesRoute> {
        val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            ?: return@get call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
        val params = call.receive<Parameters>()
        val conversationId = params["conversationId"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing conversationId")
        call.respond(HttpStatusCode.OK, messagesRepository.getMessages(conversationId))
    }

    post<ChatMessagesSendRoute> {
        val user = call.sessions.get<UserSession>()?.let { userRepository.findUser(it.userId) }
            ?: return@post call.respond(HttpStatusCode.Unauthorized, "Problems retrieving User")
        val params = call.receive<Parameters>()
        val conversationId = params["conversationId"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing conversationId")
        val content = params["content"]
            ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing content")
        val message = messagesRepository.sendMessage(conversationId, user.userId, content)
        if (message != null) {
            call.respond(HttpStatusCode.Created, message)
        } else {
            call.respond(HttpStatusCode.InternalServerError, "Failed to send message")
        }
    }
}
