package com.multiplatformkickstarter.app.feature.inbox

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.common.model.ChatMessage
import com.multiplatformkickstarter.app.data.repositories.SessionRepository
import com.multiplatformkickstarter.app.feature.inbox.repositories.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val conversation: ChatConversation,
    private val navigator: Navigator,
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(
        ChatState(
            conversation = conversation,
            currentUserId = sessionRepository.getUserId(),
        )
    )
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        loadMessages()
    }

    fun loadMessages() {
        screenModelScope.launch {
            try {
                val messages = chatRepository.getMessages(conversation.id)
                _state.value = _state.value.copy(messages = messages, error = null)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun onMessageTextChanged(text: String) {
        _state.value = _state.value.copy(messageInput = text)
    }

    fun onSendMessage() {
        val content = _state.value.messageInput.trim()
        if (content.isEmpty()) return
        screenModelScope.launch {
            _state.value = _state.value.copy(messageInput = "")
            chatRepository.sendMessage(conversation.id, content)
            loadMessages()
        }
    }

    fun onDeleteConversation() {
        screenModelScope.launch {
            chatRepository.deleteConversation(conversation.id)
            navigator.pop()
        }
    }
}

data class ChatState(
    val conversation: ChatConversation,
    val messages: List<ChatMessage> = emptyList(),
    val messageInput: String = "",
    val currentUserId: Int = -1,
    val error: String? = null,
)
