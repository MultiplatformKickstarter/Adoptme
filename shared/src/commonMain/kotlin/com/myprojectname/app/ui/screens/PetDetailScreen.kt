@file:OptIn(ExperimentalMaterial3Api::class)

package com.myprojectname.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.data.repositories.LastSearchAdsMockRepository
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.platform.shimmerLoadingAnimation
import com.myprojectname.app.ui.icon.MyProjectNameIcons
import com.myprojectname.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class PetDetailScreen(private val petId: Int) : Screen {
    @Composable
    override fun Content() {
        val currentNavigator = LocalNavigator.currentOrThrow
        val petModel = LastSearchAdsMockRepository().getAds().getOrNull()?.first { petId == it.id }
        petModel?.let {
            PetDetailView(it) {
                currentNavigator.pop()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailView(petModel: PetModel, onClose: () -> Unit) {
    val scrollState = rememberScrollState()
    val localization = getCurrentLocalization()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onClose.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = localization.backButton
                        )
                    }
                },
                title = {
                    Text(
                        text = "Back",
                        style = Typography.get().titleMedium,
                        color = Color.DarkGray
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), bottom = 16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            KamelImage(
                resource = asyncPainterResource(data = petModel.images[0]),
                contentDescription = petModel.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 18.dp)
                    .clip(RoundedCornerShape(16.dp)),
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
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = MyProjectNameIcons.Person,
                            contentDescription = "image",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = petModel.title,
                    style = Typography.get().headlineLarge,
                    color = Color.DarkGray
                )
                Text(
                    text = petModel.description,
                    style = Typography.get().bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.size(24.dp))

                Button(
                    onClick = {
                    },
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(10)
                ) {
                    Text(
                        text = localization.detailAdopt,
                        style = Typography.get().titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.size(100.dp))
            }
        }
    }
}