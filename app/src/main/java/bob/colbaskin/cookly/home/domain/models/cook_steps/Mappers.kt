package bob.colbaskin.cookly.home.domain.models.cook_steps

import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

fun RecipeDetailed.toCookStepsNavArgs(): CookStepsNavArgs {
    return CookStepsNavArgs(
        recipeId = id,
        recipeTitle = title,
        mealType = mealTime,
        recipeImageUrl = imageUrl,
        steps = steps
            .sortedBy { it.number }
            .map {
                CookStep(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    number = it.number,
                    imageUrl = it.imageUrl
                )
            }
    )
}
