package bob.colbaskin.cookly.onboarding_preferences.data.models

import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup

fun IngredientGroupDto.toDomain(): IngredientGroup {
    return IngredientGroup(
        id = this.id,
        title = this.title,
        excludedByUser = this.excludedByUser
    )
}
