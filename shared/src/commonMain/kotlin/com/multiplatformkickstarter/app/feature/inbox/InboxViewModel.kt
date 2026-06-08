package com.multiplatformkickstarter.app.feature.inbox

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.feature.inbox.repositories.ChatRepository
import com.multiplatformkickstarter.app.platform.RootNavigatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InboxViewModel(
    private val chatRepository: ChatRepository,
    private val rootNavigatorRepository: RootNavigatorRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(InboxState())
    val state: StateFlow<InboxState> = _state.asStateFlow()

    init {
        loadConversations()
    }

    fun loadConversations() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val conversations = chatRepository.getConversations()
                _state.value = _state.value.copy(conversations = conversations, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun onConversationClicked(conversation: ChatConversation) {
        rootNavigatorRepository.navigator.push(ChatScreen(conversation))
    }

    fun onDeleteConversation(conversationId: Int) {
        screenModelScope.launch {
            chatRepository.deleteConversation(conversationId)
            loadConversations()
        }
    }
}

data class InboxState(
    val conversations: List<ChatConversation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
