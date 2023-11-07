package com.myprojectname.app.ui.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.platform.Resources
import com.myprojectname.app.ui.components.CarouselItem
import com.myprojectname.app.ui.components.OnBoardingComponent
import com.myprojectname.app.ui.theme.MyProjectNameTheme

class OnboardingScreen : Screen {

    @Composable
    override fun Content() {
        val localization = getCurrentLocalization()
        val navigator = LocalNavigator.currentOrThrow
        
        MyProjectNameTheme {
            val carouselItems: List<CarouselItem> = listOf(
                CarouselItem(Resources.Drawables.catAndDog, localization.profileDescriptionPlaceholder),
                CarouselItem(Resources.Drawables.catAndDog, localization.profileDescriptionPlaceholder),
                CarouselItem(Resources.Drawables.catAndDog, localization.profileDescriptionPlaceholder),
                )
            val onBoardingComponent = OnBoardingComponent(carouselItems) {
                navigator.pop()
            }
            onBoardingComponent.DrawCarousel()
        }
    }
}
