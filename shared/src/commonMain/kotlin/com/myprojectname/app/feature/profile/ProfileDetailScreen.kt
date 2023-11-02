@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.myprojectname.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.feature.profile.viewmodels.ProfileDetailViewModel
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.platform.shimmerLoadingAnimation
import com.myprojectname.app.ui.components.EmptyLayout
import com.myprojectname.app.ui.components.PetCardSmall
import com.myprojectname.app.ui.components.RatingBar
import com.myprojectname.app.ui.icon.MyProjectNameIcons
import com.myprojectname.app.ui.theme.MyProjectNameTheme
import com.myprojectname.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.core.parameter.ParametersHolder

class ProfileDetailScreen(val userId: Int) : Screen {
    private val localization = getCurrentLocalization()

    @Composable
    override fun Content() {
        MyProjectNameTheme {
            val currentNavigator = LocalNavigator.currentOrThrow
            val viewModel = getScreenModel<ProfileDetailViewModel>(
                parameters = { ParametersHolder(listOf(userId, currentNavigator).toMutableList(), false) }
            )

            LifecycleEffect(
                onStarted = {
                    viewModel.onStarted()
                }
            )

            ProfileDetailView(viewModel, localization) {
                currentNavigator.pop()
            }
        }
    }

    @Composable
    fun ProfileDetailView(viewModel: ProfileDetailViewModel, localization: Localization, onClose: () -> Unit) {
        val scrollState = rememberScrollState()
        val state by viewModel.state.collectAsState()

        BoxWithConstraints(Modifier.fillMaxSize(), propagateMinConstraints = true) {
            val maxWidth = this.maxWidth

            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { onClose.invoke() }) {
                                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
                                }
                            },
                            title = {},
                            colors = TopAppBarDefaults.mediumTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = MyProjectNameIcons.Follow,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            if (state.image?.isEmpty() == true) {
                                Icon(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .padding(4.dp),
                                    imageVector = MyProjectNameIcons.Person,
                                    contentDescription = null
                                )
                            } else {
                                KamelImage(
                                    resource = asyncPainterResource(data = state.image!!),
                                    contentDescription = "profile image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .padding(4.dp),
                                    onLoading = {
                                        Box(
                                            modifier = Modifier
                                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                                .height(64.dp)
                                                .fillMaxWidth()
                                                .shimmerLoadingAnimation(isLoadingCompleted = false)
                                        )
                                    },
                                    onFailure = {
                                        Box(
                                            modifier = Modifier
                                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                                .height(64.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(24.dp),
                                                imageVector = MyProjectNameIcons.Person,
                                                contentDescription = "image",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = localization.profileNamePlaceholder,
                                    style = Typography.get().headlineSmall
                                )
                                Row {
                                    Text(
                                        text = state.rating.toString(),
                                        style = Typography.get().bodySmall
                                    )
                                    state.rating?.let {
                                        Spacer(modifier = Modifier.size(8.dp))
                                        RatingBar(
                                            modifier = Modifier.size(width = 80.dp, height = 16.dp),
                                            rating = it,
                                            starsColor = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                                state.description?.let {
                                    Text(
                                        modifier = Modifier.padding(top = 8.dp),
                                        text = it,
                                        style = Typography.get().bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(scrollState)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = localization.profileScreenUserAds,
                        style = Typography.get().titleMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    )

                    if (state.pets.isNotEmpty()) {
                        val itemSize: Dp = (maxWidth / 2) - 8.dp
                        FlowRow(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            maxItemsInEachRow = 2
                        ) {
                            state.pets.map { item ->
                                PetCardSmall(item = item, itemSize, itemSize) {
                                    viewModel.onPetDetailClicked(item.id)
                                }
                            }
                        }
                    } else {
                        EmptyLayout(localization = localization) {}
                    }
                }
            }
        }
    }
}
