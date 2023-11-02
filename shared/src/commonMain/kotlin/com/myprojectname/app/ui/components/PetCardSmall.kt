@file:OptIn(ExperimentalMaterial3Api::class)

package com.myprojectname.app.ui.components

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.platform.shimmerLoadingAnimation
import com.myprojectname.app.ui.icon.MyProjectNameIcons
import com.myprojectname.app.ui.theme.Typography
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun PetCardSmall(
    item: PetModel,
    width: Dp,
    height: Dp,
    onClick: () -> Unit
) {
    val imageHeight = 70.dp
    val roundedCornerSize = 10.dp

    Card(
        onClick = { onClick() },
        modifier = Modifier.size(width, height).padding(start = 16.dp, top = 16.dp),
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
                            modifier = Modifier.size(36.dp),
                            imageVector = MyProjectNameIcons.BrokenImage,
                            contentDescription = "image",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = item.title,
                style = Typography.get().titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.description,
                style = Typography.get().bodySmall,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
