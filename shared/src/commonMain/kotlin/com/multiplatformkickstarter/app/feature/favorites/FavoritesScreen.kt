package com.multiplatformkickstarter.app.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.feature.favorites.viewmodels.FavoritesViewModel
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.ui.components.EmptyLayout
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import com.multiplatformkickstarter.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.core.parameter.ParametersHolder

@OptIn(ExperimentalMaterial3Api::class)
class FavoritesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val localization = getCurrentLocalization()
        val viewModel = koinScreenModel<FavoritesViewModel>(
            parameters = { ParametersHolder(listOf(navigator).toMutableList(), false) }
        )

        DisposableEffect(key) {
            viewModel.onStarted(navigator)
            onDispose {}
        }

        val state by viewModel.state.collectAsState()

        MultiplatformKickstarterTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = localization.favorites,
                                style = Typography.get().headlineSmall,
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                    )
                },
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.TopCenter) {
                    if (state.favorites.isEmpty()) {
                        Box(modifier = Modifier.widthIn(max = 600.dp).fillMaxSize()) {
                            EmptyLayout(
                                title = localization.favoritesEmptyTitle,
                                description = localization.favoritesEmptyDescription,
                                localization = localization,
                            ) {}
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxSize(),
                        ) {
                            items(state.favorites, key = { it.id }) { pet ->
                                FavoriteItem(
                                    pet = pet,
                                    onPetClicked = { viewModel.onPetDetailClicked(pet.id) },
                                    onUnfavoriteClicked = { viewModel.onUnfavoriteClicked(pet.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(
    pet: PetModel,
    onPetClicked: () -> Unit,
    onUnfavoriteClicked: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPetClicked() },
        headlineContent = {
            Text(
                text = pet.title,
                style = Typography.get().titleMedium,
            )
        },
        supportingContent = {
            Column {
                Text(
                    text = pet.description,
                    style = Typography.get().bodySmall,
                    maxLines = 2,
                )
                Text(
                    text = pet.breed,
                    style = Typography.get().labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
        leadingContent = {
            KamelImage(
                resource = { asyncPainterResource(data = pet.images[0]) },
                contentDescription = pet.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                onLoading = {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                    )
                },
                onFailure = {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                    ) {
                        Icon(
                            imageVector = MultiplatformKickstarterIcons.Pets,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                },
            )
        },
        trailingContent = {
            IconButton(onClick = onUnfavoriteClicked) {
                Icon(
                    imageVector = MultiplatformKickstarterIcons.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
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
