package com.myprojectname.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
            val onboardingPromoLine1 = buildAnnotatedString {
                append(localization.onboardingPromoLine1)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Terms and Condition")
                }
            }

            val carouselItems: List<CarouselItem> = listOf(
                CarouselItem(Resources.Drawables.catAndDog, onboardingPromoLine1),
                CarouselItem(Resources.Drawables.catAndDog, onboardingPromoLine1),
                CarouselItem(Resources.Drawables.catAndDog, onboardingPromoLine1),
                )
            val onBoardingComponent = OnBoardingComponent(carouselItems, localization.actionSignIn) {
                navigator.pop()
            }
            onBoardingComponent.DrawCarousel()
        }
    }
}
