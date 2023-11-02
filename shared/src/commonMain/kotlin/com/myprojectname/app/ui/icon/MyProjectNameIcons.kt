package com.myprojectname.app.ui.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TextSnippet
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object MyProjectNameIcons {
    val ArrowBack = Icons.Rounded.ArrowBack
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
    val Exit = Icons.Rounded.ExitToApp
    val ArrowRight = Icons.Rounded.KeyboardArrowRight
    val Email = Icons.Rounded.Email
    val Password = Icons.Rounded.Password
    val Filter = Icons.Rounded.FilterList
    val Delete = Icons.Rounded.Delete
    val Camera = Icons.Rounded.PhotoCamera
    val Follow = Icons.Rounded.PersonAdd
    val Blog = Icons.Rounded.TextSnippet
    val Help = Icons.Rounded.Help
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
