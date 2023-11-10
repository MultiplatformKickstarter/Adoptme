package com.multiplatformkickstarter.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatformkickstarter.app.localization.getCurrentLocalization
import com.multiplatformkickstarter.app.platform.Resources
import com.multiplatformkickstarter.app.ui.components.CarouselItem
import com.multiplatformkickstarter.app.ui.components.OnboardingComponent
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme

class OnboardingScreen : Screen {

    @Composable
    override fun Content() {
        val localization = getCurrentLocalization()
        val navigator = LocalNavigator.currentOrThrow
        
        MultiplatformKickstarterTheme {
            val onboardingPromoTitle1 = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(localization.onboardingPromoTitle1)
                }
            }
            val onboardingPromoLine1 = buildAnnotatedString {
                append("Welcome to ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Multiplatform Kickstarter")
                }
                append(localization.onboardingPromoLine1)
            }

            val onboardingPromoTitle2 = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(localization.onboardingPromoTitle2)
                }
            }

            val onboardingPromoTitle3 = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(localization.onboardingPromoTitle3)
                }
            }

            val onboardingPromoLine3 = buildAnnotatedString {
                append(localization.onboardingPromoLine3)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("multiplatformkickstarter.com")
                }
            }

            val carouselItems: List<CarouselItem> = listOf(
                CarouselItem(Resources.Drawables.mkLogo, onboardingPromoTitle1, onboardingPromoLine1, localization.next),
                CarouselItem(Resources.Drawables.features, onboardingPromoTitle2, localization.onboardingPromoLine2.toAnnotatedString(), localization.next),
                CarouselItem(Resources.Drawables.productQuality, onboardingPromoTitle3, onboardingPromoLine3, localization.close) { navigator.pop() },
            )
            val onboardingComponent = OnboardingComponent(carouselItems)
            onboardingComponent.DrawCarousel()
        }
    }
}

private fun String.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString)
    }
}
