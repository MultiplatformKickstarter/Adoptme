package com.myprojectname.app.localization

import androidx.compose.runtime.Composable
import com.myprojectname.app.localization.translations.DeutschLocalization
import com.myprojectname.app.localization.translations.EnglishLocalization
import com.myprojectname.app.localization.translations.FrenchLocalization
import com.myprojectname.app.localization.translations.ItalianLocalization
import com.myprojectname.app.localization.translations.SpanishLocalization

interface Localization {
    val appName: String

    val actionSettings: String
    val next: String
    val previous: String

    val promptEmail: String
    val promptPassword: String
    val actionSignIn: String
    val actionSignInShort: String
    val welcome: String
    val invalidUsername: String
    val invalidPassword: String
    val join: String

    val genericFailedDefaultMessage: String
    val loginFailedUnauthorizedMessage: String
    val loginScreenRegisterHere: String
    val loginScreenLoginUsingGoogle: String
    val loginScreenContinueWithEmail: String
    val loginScreenNameLabel: String
    val loginScreenEmailLabel: String
    val loginScreenPasswordLabel: String
    val loginScreenStartSessionLabel: String
    val loginScreenRememberPassword: String
    val loginSuccess: String
    val loginEmailEmptyError: String
    val loginEmailFormatError: String
    val loginPasswordEmptyError: String

    val signUpCreateAnAccountButton: String
    val signUpPasswordSupport: String

    val backLabel: String
    val topAppBarActionIconDescription: String
    val home: String
    val favorites: String
    val upload: String
    val inbox: String
    val profile: String

    val onBoardingStartButton: String
    val onBoardingAlreadyHaveAnAccountButton: String
    val searchItems: String
    val backButton: String
    val search: String
    val noItemFound: String
    val noSearchHistory: String
    val start: String
    val insertAdBannerTrait: String

    val emptyScreenTitle: String
    val emptyScreenSubtitle: String
    val homeFeaturedViewAll: String
    val homePetsNearYou: String
    val homeMyLastSearch: String
    val detailAdopt: String
    val homeScreenSignUpLogin: String
    val homeScreenCategoryTitle: String

    val petUploadTitle: String
    val petUploadNext: String
    val petUploadAddImage: String
    val petUploadAddImageDescription: String
    val petUploadFormName: String
    val petUploadFormDescription: String
    val petUploadFormCategory: String
    val petUploadFormSubmit: String
    val petUploadNameField: String
    val petUploadDescriptionField: String
    val petUploadCategoryField: String
    val petUploadLocationField: String
    val petUploadBreedField: String
    val petUploadColorField: String
    val petUploadAgeField: String
    val petUploadGenderField: String
    val petUploadSizeField: String
    val petUploadStatusField: String
    val petUploadFormNameEmptyError: String
    val petUploadFormNameLengthError: String
    val petUploadFormDescriptionEmptyError: String
    val petUploadFormDescriptionLengthError: String

    val profileMyPets: String
    val profileAccountSettings: String
    val profileBlog: String
    val profileSettings: String
    val profileTermsAndConditions: String
    val profileHelp: String
    val profileCloseSession: String
    val profileScreenUserAds: String

    val profileNamePlaceholder: String
    val profileDescriptionPlaceholder: String

    val noResultsTitle: String
    val noResultsDesciption: String

    val greetingMorning: String
    val greetingAfternoon: String
    val greetingEvening: String
    val greetingNight: String

    val loginLandingTitle: String
    val loginLandingDescription: String
    val loginLandingFacebook: String
    val loginLandingGoogle: String
    val loginLandingEmail: String
    val loginLandingAlreadyHaveAccount: String
    val loginLandingLogIn: String

    val notLoggedInTitle: String
    val notLoggedInDescription: String
    val notLoggedInAction: String

    val categoryDogs: String
    val categoryCats: String
    val categoryBirds: String
    val categoryRabbits: String
    val categorySmallAndFurry: String
    val categoryHorses: String
    val categoryOther: String

    val ageBaby: String
    val ageYoung: String
    val ageAdult: String
    val ageSenior: String

    val genderMale: String
    val genderFemale: String

    val sizeSmall: String
    val sizeMedium: String
    val sizeLarge: String
    val sizeExtraLarge: String

    val statusAdoptable: String
    val statusFound: String

    val emailSignUpTermsAndConditions: String

    val debugMenuTitle: String
}

enum class AvailableLanguages {
    DE,
    EN,
    ES,
    IT,
    FR;

    companion object {
        val languages = listOf(EN, ES, IT, FR, DE)
    }
}

expect fun getCurrentLanguage(): AvailableLanguages

@Composable
expect fun SetLanguage(language: AvailableLanguages)

fun getCurrentLocalization() = when (getCurrentLanguage()) {
    AvailableLanguages.EN -> EnglishLocalization
    AvailableLanguages.ES -> SpanishLocalization
    AvailableLanguages.IT -> ItalianLocalization
    AvailableLanguages.FR -> FrenchLocalization
    AvailableLanguages.DE -> DeutschLocalization
}

fun getLocalizedModelName(name: String): String {
    val localization = getCurrentLocalization()
    return when (name) {
        "DOGS" -> localization.categoryDogs
        "CATS" -> localization.categoryCats
        "BIRDS" -> localization.categoryBirds
        "RABBITS" -> localization.categoryRabbits
        "SMALL_AND_FURRY" -> localization.categorySmallAndFurry
        "HORSES" -> localization.categoryHorses
        "OTHER" -> localization.categoryOther
        "BABY" -> localization.ageBaby
        "YOUNG" -> localization.ageYoung
        "ADULT" -> localization.ageAdult
        "SENIOR" -> localization.ageSenior
        "MALE" -> localization.genderMale
        "FEMALE" -> localization.genderFemale
        "SMALL" -> localization.sizeSmall
        "MEDIUM" -> localization.sizeMedium
        "LARGE" -> localization.sizeLarge
        "XLARGE" -> localization.sizeExtraLarge
        "ADOPTABLE" -> localization.statusAdoptable
        "FOUND" -> localization.statusFound
        else -> {
            throw Exception()
        }
    }
}
