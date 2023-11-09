package com.myprojectname.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
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
                append("Welcome to ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Multiplatform Kickstarter")
                }
                append(localization.onboardingPromoLine1)
            }

            val onboardingPromoLine3 = buildAnnotatedString {
                append(localization.onboardingPromoLine3)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("multiplatformkickstarter.com")
                }
            }

            val carouselItems: List<CarouselItem> = listOf(
                CarouselItem(Resources.Drawables.catAndDog, localization.onboardingPromoTitle1.toAnnotatedString(), onboardingPromoLine1),
                CarouselItem(Resources.Drawables.catAndDog, localization.onboardingPromoTitle2.toAnnotatedString(), localization.onboardingPromoLine2.toAnnotatedString()),
                CarouselItem(Resources.Drawables.catAndDog, localization.onboardingPromoTitle3.toAnnotatedString(), onboardingPromoLine3),
                )
            val onBoardingComponent = OnBoardingComponent(carouselItems, localization.actionSignIn) {
                navigator.pop()
            }
            onBoardingComponent.DrawCarousel()
        }
    }
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString)
    }
}
