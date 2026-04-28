package bob.colbaskin.cookly.home.domain.models.old

import androidx.annotation.DrawableRes

data class Meal(
    val id: Int,
    @param:DrawableRes val imageId: Int, // TODO: заменить потом на ссыллку
    val title: String,
    val description: String
)
