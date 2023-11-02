@file:OptIn(ExperimentalMaterial3Api::class)

package com.myprojectname.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.myprojectname.app.localization.Localization

@Composable
fun PetsSearchBar(
    modifier: Modifier = Modifier,
    localization: Localization
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchResults = remember { mutableStateListOf<Any>() }

    LaunchedEffect(query) {
        searchResults.clear()
        if (query.isNotEmpty()) {
            /*searchResults.addAll(
                items.filter {
                    it.subject.startsWith(
                        prefix = query,
                        ignoreCase = true
                    ) || it.sender.fullName.startsWith(
                        prefix =
                        query,
                        ignoreCase = true
                    )
                }
            )*/
        }
    }

    DockedSearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = {
            query = it
        },
        onSearch = { active = false },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = localization.searchItems)
        },
        leadingIcon = {
            if (active) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = localization.backButton,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .clickable {
                            active = false
                            query = ""
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = localization.search,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                )
            }
        }
    ) {
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                /*items(items = searchResults, key = { it.id }) { email ->
                    ListItem(
                        headlineContent = { Text(email.subject) },
                        supportingContent = { Text(email.sender.fullName) },
                        leadingContent = {
                            ProfileImage(
                                drawableResource = email.sender.avatar,
                                description = stringResource(id = R.string.profile),
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        },
                        modifier = Modifier.clickable {
                            onSearchItemSelected.invoke(email)
                            query = ""
                            active = false
                        }
                    )
                }*/
            }
        } else if (query.isNotEmpty()) {
            Text(
                text = localization.noItemFound,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Text(
                text = localization.noSearchHistory,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    drawableResource: Int? = null,
    imageVector: ImageVector? = Icons.Default.AccountCircle,
    description: String
) {
    if (drawableResource != null) {
        /*Image(
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape),
            painter = painterResource(id = drawableResource),
            contentDescription = description,
        )*/
    } else {
        imageVector?.let {
            Icon(
                modifier = modifier
                    .size(40.dp)
                    .clip(CircleShape),
                imageVector = it,
                contentDescription = description
            )
        }
    }
}

/*@Preview
@Composable
fun DockedSearchBarPreview() {
    MyProjectNameTheme {
        AdsSearchBar(items = emptyList(), onSearchItemSelected = {})
    }
}*/
