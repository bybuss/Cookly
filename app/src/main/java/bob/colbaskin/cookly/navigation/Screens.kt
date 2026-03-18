package bob.colbaskin.cookly.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {

    @Serializable
    data object WebViewAuth: Screens

    @Serializable
    data object Preferences

    @Serializable
    data object Home: Screens

    @Serializable
    data object Cart: Screens

    @Serializable
    data object Chat: Screens

    @Serializable
    data object Favourites: Screens

    @Serializable
    data object Profile: Screens

    @Serializable
    data object Agreement: Screens

    @Serializable
    data object Policy: Screens

    @Serializable
    data object TermsOfUse: Screens

}
