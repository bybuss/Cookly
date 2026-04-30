package bob.colbaskin.cookly.onboarding_preferences.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExcludeIngredientGroupsBody(
    @SerialName("ingredient_group_ids")
    val ingredientGroupIds: List<Int>
)
