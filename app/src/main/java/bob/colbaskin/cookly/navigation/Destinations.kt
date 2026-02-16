package bob.colbaskin.cookly.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import bob.colbaskin.cookly.R


enum class Destinations (
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val screen: Screens
) {
    HOME(
        icon = R.drawable.home_ic,
        label = R.string.home_nav_label,
        screen = Screens.Home
    ),
    CART(
        icon = R.drawable.cart_ic,
        label = R.string.cart_nav_label,
        screen = Screens.Cart
    ),
    CHAT(
        icon = R.drawable.chat_ic,
        label = R.string.chat_nav_label,
        screen = Screens.Chat
    ),
    FAVOURITES(
        icon = R.drawable.heart_ic,
        label = R.string.favourites_nav_label,
        screen = Screens.Favourites
    ),
    PROFILE(
        icon = R.drawable.user_ic,
        label = R.string.profile_nav_label,
        screen = Screens.Profile
    )
}
