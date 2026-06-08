@file:OptIn(ExperimentalMaterial3Api::class)

package com.multiplatformkickstarter.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.platform.shimmerLoadingAnimation
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.screens.viewmodel.PetDetailViewModel
import com.multiplatformkickstarter.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.core.parameter.ParametersHolder

class PetDetailScreen(private val petId: Int) : Screen {
    @Composable
    override fun Content() {
        val currentNavigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<PetDetailViewModel>(
            parameters = { ParametersHolder(listOf(petId, currentNavigator).toMutableList(), false) }
        )
        val state by viewModel.state.collectAsState()

        state.pet?.let { pet ->
            PetDetailView(
                petTitle = pet.title,
                petDescription = pet.description,
                petImageUrl = pet.images[0],
                isFavorite = state.isFavorite,
                onFavoriteToggled = { viewModel.onFavoriteToggled() },
                onAdoptClicked = { viewModel.onAdoptClicked() },
                onClose = { currentNavigator.pop() },
            )
        }
    }
}

@Composable
fun PetDetailView(
    petTitle: String,
    petDescription: String,
    petImageUrl: String,
    isFavorite: Boolean,
    onFavoriteToggled: () -> Unit,
    onAdoptClicked: () -> Unit,
    onClose: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val localization = getCurrentLocalization()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onClose.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = localization.backButton,
                        )
                    }
                },
                title = {},
                actions = {
                    IconButton(onClick = onFavoriteToggled) {
                        Icon(
                            imageVector = if (isFavorite) {
                                MultiplatformKickstarterIcons.Favorite
                            } else {
                                MultiplatformKickstarterIcons.FavoriteOutlined
                            },
                            contentDescription = null,
                            tint = if (isFavorite) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
        ) {
            KamelImage(
                resource = { asyncPainterResource(data = petImageUrl) },
                contentDescription = petTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(250.dp),
                onLoading = {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .height(250.dp)
                            .fillMaxWidth()
                            .shimmerLoadingAnimation(isLoadingCompleted = false)
                    )
                },
                onFailure = {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .height(250.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = MultiplatformKickstarterIcons.BrokenImage,
                            contentDescription = "image",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = petTitle,
                    style = Typography.get().headlineLarge,
                    color = Color.DarkGray
                )
                Text(
                    text = petDescription,
                    style = Typography.get().bodyMedium,
                    color = Color.DarkGray
                )
            }
            Button(
                onClick = onAdoptClicked,
                colors = ButtonDefaults.buttonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp)
                    .padding(start = 16.dp, end = 16.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(10)
            ) {
                Text(
                    text = localization.detailAdopt,
                    style = Typography.get().titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        }
    }
}
