package com.myprojectname.app.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
expect fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font

object Resources {
    object Drawables {
        const val mindMap = "drawable/mind_map.xml"
        const val noData = "drawable/no_data_cuate.xml"
        const val login = "drawable/authentication_rafiki.xml"
        const val catAndDog = "drawable/cat_dog.xml"
    }
}
