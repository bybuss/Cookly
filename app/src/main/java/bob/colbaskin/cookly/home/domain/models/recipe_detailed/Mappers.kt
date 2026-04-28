package bob.colbaskin.cookly.home.domain.models.recipe_detailed

import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedDto
import bob.colbaskin.cookly.home.presentation.recipe_detailed.RecipeDetailedState
import kotlin.math.roundToInt

fun RecipeDetailedDto.toDomain(): RecipeDetailed {
    return RecipeDetailed(
        id = id,
        title = title,
        description = description,
        estimatedTime = estimatedTime,
        caloriesBy100Grams = caloriesBy100Grams.roundToInt(),
        mealTime = mealTime,
        rating = calculateRating(
            ratingSum = ratingSum,
            ratingCount = ratingCount
        ),
        ratingCount = ratingCount,
        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,
        imageUrl = imageUrl,
        ingredients = recipeIngredients.map { item ->
            Ingredient(
                name = item.ingredient.title,
                count = item.quantity.toIngredientCount(),
                unitOfMeasurement = item.unitMeasurement
            )
        },
        categories = recipeCategories.map { it.title }
    )
}

private fun calculateRating(
    ratingSum: Double,
    ratingCount: Int
): Double {
    if (ratingCount <= 0) return 0.0
    return ratingSum / ratingCount
}

private fun Double.toIngredientCount(): Int {
    return roundToInt()
}

fun String.toDomainMealTime(isPlural: Boolean): String {
    return when (this) {
        "breakfast" ->  if (isPlural) "Завтраки" else "Завтрак"
        "lunch" -> if (isPlural) "Обеды" else "Обед"
        "supper" -> if (isPlural) "Ужины" else "Ужин"
        else -> "unknown"
    }
}
