package bob.colbaskin.cookly.create_recipe.domain.models

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.recipe_preview.domain.models.MealTimeType
import bob.colbaskin.cookly.create_recipe.presentation.CreateRecipeState
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

fun RecipeDetailed.toEditRecipeState(
    currentState: CreateRecipeState
): CreateRecipeState {
    val hours = estimatedTime / 60
    val minutes = estimatedTime % 60

    return currentState.copy(
        mode = RecipeFormMode.EDIT,
        recipeId = id,
        isInitialLoading = false,
        initialLoadState = UiState.Success(Unit),

        title = title,
        description = description,
        estimatedHour = hours,
        estimatedMinute = minutes,
        caloriesBy100Grams = caloriesBy100Grams.toString(),
        mealTimeType = mealTime.toCreateRecipeMealTimeTypeOrNull(),

        categories = categories.map {
            CreateRecipeCategory(
                categoryId = it.id,
                title = it.title
            )
        },

        ingredients = ingredients.map {
            CreateRecipeIngredient(
                ingredientId = it.id ?: 0,
                title = it.name,
                quantity = it.count.toString(),
                unitMeasurement = it.unitOfMeasurement
            )
        }.filter { it.ingredientId > 0 },

        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,

        steps = steps
            .sortedBy { it.number }
            .mapIndexed { index, step ->
                CreateRecipeStep(
                    localId = index + 1L,
                    number = step.number,
                    title = step.title,
                    description = step.description,
                    image = null,
                    existingImageUrl = step.imageUrl
                )
            }
            .ifEmpty {
                listOf(CreateRecipeStep(localId = 1L, number = 1))
            },

        mainPhoto = null,
        existingMainPhotoUrl = imageUrl
    )
}

private fun MealTimeType?.safeApiValue(): String? = this?.apiValue

private fun String.toCreateRecipeMealTimeTypeOrNull(): MealTimeType? {
    return MealTimeType.entries.firstOrNull { it.apiValue == this }
}
