@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.multiplatformkickstarter.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.localization.Localization
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.localization.getLocalizedModelName
import com.multiplatformkickstarter.app.ui.components.EmptyLayout
import com.multiplatformkickstarter.app.ui.components.PetCardBig
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.screens.viewmodel.SearchListingSideEffects
import com.multiplatformkickstarter.app.ui.screens.viewmodel.SearchListingViewModel
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import com.multiplatformkickstarter.app.ui.theme.Typography
import kotlinx.coroutines.launch
import org.koin.core.parameter.ParametersHolder

class SearchListingScreen(
    private val searchId: Int? = null,
    private val petCategory: PetCategory? = null
) : Screen {

    @Composable
    override fun Content() {
        val localization = getCurrentLocalization()
        val snackbarHostState = remember { SnackbarHostState() }
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = koinScreenModel<SearchListingViewModel>(
            parameters = {
                ParametersHolder(
                    listOf(
                        searchId,
                        petCategory,
                        navigator
                    ).toMutableList(), false
                )
            }
        )

        DisposableEffect(key) {
            viewModel.onStarted()
            onDispose {}
        }

        SetupSideEffects(viewModel, snackbarHostState, localization)
        MultiplatformKickstarterTheme {
            SearchListingView(viewModel) {
                navigator.pop()
            }
        }
    }

    @Composable
    private fun SetupSideEffects(
        viewModel: SearchListingViewModel,
        snackbarHostState: SnackbarHostState,
        localization: Localization
    ) {
        val coroutineScope = rememberCoroutineScope()
        val sideEffects = viewModel.sideEffects.collectAsState(SearchListingSideEffects.Initial)
        sideEffects.value.apply {
            when (this) {
                SearchListingSideEffects.Initial -> {}
                is SearchListingSideEffects.OnLoaded -> {}
                SearchListingSideEffects.OnLoadError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(localization.genericFailedDefaultMessage)
                    }
                }

                SearchListingSideEffects.OnNoResults -> {}
            }
        }
    }

    @Composable
    fun SearchListingView(viewModel: SearchListingViewModel, onClose: () -> Unit) {
        val localization = getCurrentLocalization()
        val scrollState = rememberScrollState()
        val state by viewModel.state.collectAsState()
        val topBarTitle = if (petCategory != null) {
            getLocalizedModelName(petCategory.name)
        } else {
            localization.homeMyLastSearch
        }

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
                    title = {
                        Text(
                            text = topBarTitle,
                            style = Typography.get().titleMedium,
                        )
                    },
                    colors =
                        TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = MultiplatformKickstarterIcons.Filter,
                                contentDescription = localization.backLabel,
                            )
                        }
                    },
                )
            }
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(bottom = 16.dp)
                        .verticalScroll(scrollState)
                        .background(MaterialTheme.colorScheme.background),
            ) {
                if (state.pets.isNotEmpty()) {
                    state.pets.map { petModel ->
                        PetCardBig(item = petModel) {
                            viewModel.onPetClicked(petModel.id)
                        }
                    }
                } else {
                    EmptyLayout(localization = localization) {}
                }
            }
        }
    }
}
