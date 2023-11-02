@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.myprojectname.app.feature.pets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.feature.pets.viewmodels.MyPetsViewModel
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.ui.components.EmptyLayout
import com.myprojectname.app.ui.components.PetCardSmall
import com.myprojectname.app.ui.screens.PetDetailScreen
import com.myprojectname.app.ui.theme.Typography
import org.koin.core.parameter.ParametersHolder

class MyPetsScreen : Screen {
    @Composable
    override fun Content() {
        val localization = getCurrentLocalization()
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getScreenModel<MyPetsViewModel>(
            parameters = { ParametersHolder(listOf(navigator).toMutableList(), false) }
        )
        MyPetsScreenView(viewModel, localization) {
            navigator.pop()
        }
    }
}

@Composable
fun MyPetsScreenView(viewModel: MyPetsViewModel, localization: Localization, onClose: () -> Unit) {
    val currentNavigator = LocalNavigator.currentOrThrow
    val scrollState = rememberScrollState()

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
                        title = {
                            Text(
                                text = localization.profileMyPets,
                                style = Typography.get().titleMedium
                            )
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
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
                if (viewModel.state.value.ads.isNotEmpty()) {
                    val itemSize: Dp = (maxWidth / 2) - 8.dp
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        maxItemsInEachRow = 2
                    ) {
                        viewModel.state.value.ads.map { item ->
                            PetCardSmall(item = item, itemSize, itemSize) {
                                currentNavigator.push(PetDetailScreen(item.id))
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
