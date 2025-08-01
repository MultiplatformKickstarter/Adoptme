package com.multiplatformkickstarter.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.multiplatformkickstarter.app.localization.Localization
import com.multiplatformkickstarter.app.resources.Res
import com.multiplatformkickstarter.app.resources.no_data_cuate
import com.multiplatformkickstarter.app.ui.theme.Typography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EmptyLayout(
    title: String? = null,
    description: String? = null,
    imageResource: DrawableResource? = null,
    actionLabel: String? = null,
    localization: Localization,
    action: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = title ?: localization.noResultsTitle,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = description ?: localization.noResultsDesciption,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
        )
        Image(
            imageVector = vectorResource(imageResource ?: Res.drawable.no_data_cuate),
            contentDescription = description,
            modifier = Modifier.size(260.dp),
        )
        actionLabel?.let {
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                onClick = {
                    action.invoke()
                },
                colors = ButtonDefaults.buttonColors(),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .size(50.dp)
                        .padding(start = 16.dp, end = 16.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(10),
            ) {
                Text(
                    text = it,
                    style = Typography.get().titleMedium,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}
