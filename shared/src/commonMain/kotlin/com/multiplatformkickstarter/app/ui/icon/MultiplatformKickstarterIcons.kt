package com.multiplatformkickstarter.app.ui.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.TextSnippet
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object MultiplatformKickstarterIcons {
    val ArrowBack = Icons.AutoMirrored.Filled.ArrowBack
    val ArrowDropDown = Icons.Rounded.ArrowDropDown
    val Bookmark = Icons.Rounded.Favorite
    val Check = Icons.Rounded.Check
    val Person = Icons.Rounded.Face
    val Settings = Icons.Rounded.Settings
    val Info = Icons.Rounded.Info
    val Home = Icons.Rounded.Home
    val Create = Icons.Rounded.Create
    val Inbox = Icons.Rounded.Email
    val Favorite = Icons.Rounded.Favorite
    val Exit = Icons.AutoMirrored.Rounded.ExitToApp
    val ArrowRight = Icons.AutoMirrored.Rounded.KeyboardArrowRight
    val Email = Icons.Rounded.Email
    val Password = Icons.Rounded.Password
    val Filter = Icons.Rounded.FilterList
    val Delete = Icons.Rounded.Delete
    val Camera = Icons.Rounded.PhotoCamera
    val Follow = Icons.Rounded.PersonAdd
    val Blog = Icons.AutoMirrored.Rounded.TextSnippet
    val Help = Icons.AutoMirrored.Rounded.Help
    val Pets = Icons.Rounded.Pets
    val BrokenImage = Icons.Rounded.BrokenImage
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(val id: Int) : Icon()
}
