@file:OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)

package com.myprojectname.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.feature.debugmenu.getDebug
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.localization.getCurrentLanguage
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.localization.getLocalizedModelName
import com.myprojectname.app.localization.SetLanguage
import com.myprojectname.app.platform.Resources
import com.myprojectname.app.ui.components.EmptyLayout
import com.myprojectname.app.ui.components.PetCardSmall
import com.myprojectname.app.ui.components.PetsSearchBar
import com.myprojectname.app.ui.screens.viewmodel.HomeScreenState
import com.myprojectname.app.ui.screens.viewmodel.HomeScreenViewModel
import com.myprojectname.app.ui.theme.Typography
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.ParametersHolder

class HomeTabScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<HomeScreenViewModel>(
            parameters = { ParametersHolder(listOf(navigator).toMutableList(), false) }
        )

        LifecycleEffect(
            onStarted = {
                viewModel.onStarted(navigator)
            }
        )

        HomeScreenView(viewModel)
    }

    @Composable
    fun HomeScreenView(viewModel: HomeScreenViewModel) {
        val scrollState = rememberScrollState()
        val localization = getCurrentLocalization()
        val state by viewModel.state.collectAsState()
        val debug = getDebug()

        if (state.currentLanguage != getCurrentLanguage()) {
            SetLanguage(state.currentLanguage)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .clickable { viewModel.onSignUpLoginClicked() }
                        ) {
                            Text(
                                text = state.greeting,
                                style = Typography.get().labelSmall
                            )
                            Text(
                                text = state.header,
                                style = Typography.get().bodyLarge
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = localization.backLabel
                            )
                        }
                        if (debug.isDebug) {
                            IconButton(
                                onClick = { viewModel.onDebugMenuClicked() }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = localization.backLabel
                                )
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->

            BoxWithConstraints(Modifier.fillMaxSize().padding(paddingValues), propagateMinConstraints = true) {
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PetsSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        localization = localization
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        BannerHeader(viewModel, localization)
                        CategoriesCarousel(viewModel, localization)
                        LastSearchCarousel(viewModel, localization, state)
                        NearMeListView(viewModel, state, localization, maxWidth)
                    }
                }
            }
        }
    }

    @Composable
    fun BannerHeader(viewModel: HomeScreenViewModel, localization: Localization) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painterResource(Resources.Drawables.catAndDog),
                    contentDescription = "",
                    modifier = Modifier.size(150.dp)
                )
                Column {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
                            .align(Alignment.End),
                        text = localization.insertAdBannerTrait,
                        style = Typography.get().bodySmall
                    )
                    Button(
                        onClick = {
                            viewModel.onUploadPetClicked()
                        },
                        colors = ButtonDefaults.buttonColors(),
                        modifier = Modifier
                            .size(270.dp, 50.dp)
                            .padding(start = 16.dp, end = 16.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(
                            text = localization.start,
                            style = Typography.get().titleMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CategoriesCarousel(viewModel: HomeScreenViewModel, localization: Localization) {
        Text(
            text = localization.homeScreenCategoryTitle,
            style = Typography.get().titleMedium,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(PetCategory.entries.size) { index ->
                Text(
                    text = getLocalizedModelName(PetCategory.entries[index].name),
                    style = Typography.get().bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 4.dp)
                        .clickable {
                            viewModel.onCategoryClicked(PetCategory.entries[index])
                        },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
                if (index == PetCategory.entries.size - 1) {
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }

    @Composable
    fun LastSearchCarousel(viewModel: HomeScreenViewModel, localization: Localization, state: HomeScreenState) {
        if (state.lastSearchAds.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = localization.homeMyLastSearch,
                    style = Typography.get().titleMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
                Text(
                    text = localization.homeFeaturedViewAll,
                    style = Typography.get().titleMedium,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .clickable {
                            viewModel.onViewSearchClicked(0)
                        }
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.lastSearchAds.size) { index ->
                    PetCardSmall(state.lastSearchAds[index], 160.dp, 180.dp) {
                        viewModel.onPetDetailClicked(state.lastSearchAds[index].id)
                    }
                    if (index == state.lastSearchAds.size - 1) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun NearMeListView(viewModel: HomeScreenViewModel, state: HomeScreenState, localization: Localization, maxWidth: Dp) {
        Text(
            text = localization.homePetsNearYou,
            style = Typography.get().titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        if (state.nearMeAds.isNotEmpty()) {
            val itemSize: Dp = (maxWidth / 2) - 8.dp
            FlowRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                maxItemsInEachRow = 2
            ) {
                state.nearMeAds.map { item ->
                    PetCardSmall(item = item, itemSize, itemSize) {
                        viewModel.onPetDetailClicked(item.id)
                    }
                }
            }
            if (state.nearMeAds.isNotEmpty()) {
                Spacer(modifier = Modifier.size(100.dp))
            }
        } else {
            EmptyLayout(localization = localization) {}
        }
    }
}
