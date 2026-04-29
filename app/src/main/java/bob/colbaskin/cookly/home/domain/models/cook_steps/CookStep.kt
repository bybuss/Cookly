package bob.colbaskin.cookly.home.domain.models.cook_steps

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CookStep(
    val id: Int,
    val title: String,
    val description: String,
    val number: Int,
    val imageUrl: String?
): Parcelable
