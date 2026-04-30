package bob.colbaskin.cookly.onboarding_preferences.data.models

import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup

fun IngredientGroupDto.toDomain(): IngredientGroup {
    return IngredientGroup(
        id = this.id,
        title = this.title
    )
}

fun IngredientGroup.toDto(): IngredientGroupDto {
    return IngredientGroupDto(
        id = this.id,
        title = this.title
    )
}
