package com.multiplatformkickstarter.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.localization.Localization
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.platform.shimmerLoadingAnimation
import com.multiplatformkickstarter.app.resources.Res
import com.multiplatformkickstarter.app.resources.authentication_rafiki
import com.multiplatformkickstarter.app.ui.components.EmptyLayout
import com.multiplatformkickstarter.app.ui.components.PickerItem
import com.multiplatformkickstarter.app.ui.components.RatingBar
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.screens.viewmodel.ProfileState
import com.multiplatformkickstarter.app.ui.screens.viewmodel.ProfileViewModel
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import com.multiplatformkickstarter.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.core.parameter.ParametersHolder

class ProfileTabScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val localization = getCurrentLocalization()

        val viewModel = koinScreenModel<ProfileViewModel>(
            parameters = { ParametersHolder(listOf(navigator).toMutableList(), false) }
        )

        DisposableEffect(key) {
            viewModel.onStarted(navigator)
            onDispose {}
        }

        val state by viewModel.state.collectAsState()
        if (state.isLoggedIn) {
            ProfileScreenView(viewModel, state, localization)
        } else {
            EmptyLayout(
                title = localization.notLoggedInTitle,
                description = localization.notLoggedInDescription,
                actionLabel = localization.notLoggedInAction,
                imageResource = Res.drawable.authentication_rafiki,
                localization = localization
            ) {
                viewModel.onSignUpLoginClicked()
            }
        }
    }

    @Composable
    fun ProfileScreenView(
        viewModel: ProfileViewModel,
        state: ProfileState,
        localization: Localization,
    ) {
        val scrollState = rememberScrollState()
        MultiplatformKickstarterTheme {
            Column(modifier = Modifier.padding(bottom = 80.dp).verticalScroll(scrollState)) {
                UserProfileData(state, viewModel)
                OptionsList(localization, viewModel)
            }
        }
    }

    @Composable
    fun UserProfileData(
        state: ProfileState,
        viewModel: ProfileViewModel,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    viewModel.onProfileDetailClicked()
                },
            horizontalArrangement = Arrangement.Start,
        ) {
            if (state.image.isEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(4.dp),
                    imageVector = MultiplatformKickstarterIcons.Person,
                    contentDescription = null,
                )
            } else {
                KamelImage(
                    resource = asyncPainterResource(data = state.image),
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
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = MultiplatformKickstarterIcons.Person,
                                contentDescription = "image",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
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
                    text = state.name,
                    style = Typography.get().headlineSmall
                )
                Row {
                    Text(
                        text = state.rating.toString(),
                        style = Typography.get().bodySmall
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    RatingBar(
                        modifier = Modifier.size(width = 80.dp, height = 16.dp),
                        rating = state.rating,
                        starsColor = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = state.description,
                    style = Typography.get().bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                imageVector = MultiplatformKickstarterIcons.ArrowRight,
                contentDescription = null
            )
        }
    }

    @Composable
    fun OptionsList(localization: Localization, viewModel: ProfileViewModel) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PickerItem(
                localization.profileMyPets,
                MultiplatformKickstarterIcons.Pets,
            ) { viewModel.onMyPetsClicked() }
            PickerItem(
                localization.profileAccountSettings,
                MultiplatformKickstarterIcons.Settings,
            ) { viewModel.onProfileAccountSettingsClicked() }
            PickerItem(
                localization.profileSettings,
                MultiplatformKickstarterIcons.Settings,
            ) { viewModel.onProfileSettingsClicked() }
            PickerItem(
                localization.profileBlog,
                MultiplatformKickstarterIcons.Blog,
            ) { viewModel.onBlogClicked() }
            PickerItem(
                localization.profileTermsAndConditions,
                MultiplatformKickstarterIcons.Info,
            ) { viewModel.onTermsAndConditionsClicked() }
            PickerItem(
                localization.profileHelp,
                MultiplatformKickstarterIcons.Help,
            ) { viewModel.onHelpClicked() }
            PickerItem(
                localization.profileCloseSession,
                MultiplatformKickstarterIcons.Exit,
            ) { viewModel.onCloseSessionClicked() }
        }
    }
}
