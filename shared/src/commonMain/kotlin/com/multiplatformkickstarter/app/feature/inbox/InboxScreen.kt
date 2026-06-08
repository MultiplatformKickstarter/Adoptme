@file:OptIn(ExperimentalMaterial3Api::class)

package com.multiplatformkickstarter.app.feature.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.common.model.ChatConversation
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.components.EmptyLayout
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import com.multiplatformkickstarter.app.ui.theme.Typography
import org.koin.core.parameter.ParametersHolder

class InboxScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val localization = getCurrentLocalization()
        val viewModel = koinScreenModel<InboxViewModel>(
            parameters = { ParametersHolder(listOf(navigator).toMutableList(), false) }
        )
        val state by viewModel.state.collectAsState()

        MultiplatformKickstarterTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = localization.inbox,
                                style = Typography.get().headlineSmall,
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                    )
                },
            ) { paddingValues ->
                if (state.error != null) {
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        EmptyLayout(
                            title = localization.inboxEmptyTitle,
                            description = state.error ?: localization.inboxEmptyDescription,
                            localization = localization,
                        ) { viewModel.loadConversations() }
                    }
                } else if (state.conversations.isEmpty()) {
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        EmptyLayout(
                            title = localization.inboxEmptyTitle,
                            description = localization.inboxEmptyDescription,
                            localization = localization,
                        ) {}
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    ) {
                        items(state.conversations, key = { it.id }) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                onClick = { viewModel.onConversationClicked(conversation) },
                                onDelete = { viewModel.onDeleteConversation(conversation.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(
    conversation: ChatConversation,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val localization = getCurrentLocalization()
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        headlineContent = {
            Text(
                text = "${localization.inboxConversationWith} ${conversation.petName}",
                style = Typography.get().titleMedium,
            )
        },
        supportingContent = {
            Text(
                text = "${localization.chatStartedFor} ${conversation.petName}",
                style = Typography.get().bodySmall,
            )
        },
        leadingContent = {
            Icon(
                imageVector = MultiplatformKickstarterIcons.Inbox,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = MultiplatformKickstarterIcons.Delete,
                    contentDescription = localization.chatDeleteConversation,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}
