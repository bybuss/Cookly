package bob.colbaskin.cookly.navigation

import kotlinx.serialization.Serializable

interface Screens {
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
}
