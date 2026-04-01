package bob.colbaskin.cookly.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import bob.colbaskin.cookly.R

enum class Destinations (
    @param:DrawableRes val outlinedIcon: Int,
    @param:DrawableRes val filledIcon: Int = outlinedIcon,
    @param:StringRes val label: Int,
    val screen: Screens
) {
    HOME(
        outlinedIcon = R.drawable.home_ic,
        label = R.string.home_nav_label,
        screen = Screens.Home
    ),
    FAVOURITES(
        outlinedIcon = R.drawable.heart_ic,
        label = R.string.favourites_nav_label,
        screen = Screens.Favourites
    ),
    CHAT(
        outlinedIcon = R.drawable.chat_ic,
        label = R.string.chat_nav_label,
        screen = Screens.Chat
    ),
    CART(
        outlinedIcon = R.drawable.cart_ic,
        label = R.string.cart_nav_label,
        screen = Screens.Cart
    ),
    PROFILE(
        outlinedIcon = R.drawable.user_ic,
        label = R.string.profile_nav_label,
        screen = Screens.Profile
    )
}
