@file:OptIn(ExperimentalMaterial3Api::class)

package com.multiplatformkickstarter.app.feature.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.common.model.ChatMessage
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import com.multiplatformkickstarter.app.ui.theme.Typography
import org.koin.core.parameter.ParametersHolder

class ChatScreen(private val conversation: ChatConversation) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val localization = getCurrentLocalization()
        val viewModel = koinScreenModel<ChatViewModel>(
            parameters = { ParametersHolder(listOf(conversation, navigator).toMutableList(), false) }
        )
        val state by viewModel.state.collectAsState()

        var showDeleteDialog by remember { mutableStateOf(false) }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(localization.chatDeleteConversation) },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        viewModel.onDeleteConversation()
                    }) {
                        Text(localization.chatDeleteConversation)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(localization.backLabel)
                    }
                },
            )
        }

        MultiplatformKickstarterTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { navigator.pop() }) {
                                Icon(
                                    imageVector = MultiplatformKickstarterIcons.ArrowBack,
                                    contentDescription = localization.backButton,
                                )
                            }
                        },
                        title = {
                            Text(
                                text = "${localization.inboxConversationWith} ${conversation.petName}",
                                style = Typography.get().titleMedium,
                            )
                        },
                        actions = {
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(
                                    imageVector = MultiplatformKickstarterIcons.Delete,
                                    contentDescription = localization.chatDeleteConversation,
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                    )
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .imePadding(),
                ) {
                    val listState = rememberLazyListState()
                    LaunchedEffect(state.messages.size) {
                        if (state.messages.isNotEmpty()) {
                            listState.animateScrollToItem(state.messages.size - 1)
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.messages, key = { it.id }) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.senderUserId == state.currentUserId,
                            )
                        }
                    }

                    MessageInputBar(
                        value = state.messageInput,
                        onValueChange = { viewModel.onMessageTextChanged(it) },
                        onSend = { viewModel.onSendMessage() },
                        hint = localization.chatMessageHint,
                        sendLabel = localization.chatSendMessage,
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isCurrentUser) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                        bottomEnd = if (isCurrentUser) 4.dp else 16.dp,
                    ),
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = message.content,
                style = Typography.get().bodyMedium,
                color = if (isCurrentUser) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}

@Composable
private fun MessageInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    hint: String,
    sendLabel: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(hint) },
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            maxLines = 4,
        )
        IconButton(
            onClick = onSend,
            enabled = value.isNotBlank(),
        ) {
            Icon(
                imageVector = MultiplatformKickstarterIcons.Create,
                contentDescription = sendLabel,
                tint = if (value.isNotBlank()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                },
            )
        }
    }
}
