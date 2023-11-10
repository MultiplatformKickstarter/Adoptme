@file:OptIn(ExperimentalMaterial3Api::class)

package com.multiplatformkickstarter.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.platform.shimmerLoadingAnimation
import com.multiplatformkickstarter.app.ui.icon.MultiplatformKickstarterIcons
import com.multiplatformkickstarter.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun PetCardBig(
    item: PetModel,
    onClick: () -> Unit
) {
    val imageHeight = 120.dp
    val roundedCornerSize = 10.dp

    Card(
        onClick = { onClick() },
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(roundedCornerSize),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        interactionSource = remember { MutableInteractionSource() }
    ) {
        Column {
            KamelImage(
                resource = asyncPainterResource(data = item.images[0]),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(roundedCornerSize))
                    .height(imageHeight),
                onLoading = {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(roundedCornerSize))
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .height(imageHeight)
                            .fillMaxWidth()
                            .shimmerLoadingAnimation(isLoadingCompleted = false)
                    )
                },
                onFailure = {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(roundedCornerSize))
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .height(imageHeight)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            imageVector = MultiplatformKickstarterIcons.BrokenImage,
                            contentDescription = "image",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
            Text(
                text = item.title,
                style = Typography.get().headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.description,
                style = Typography.get().bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
