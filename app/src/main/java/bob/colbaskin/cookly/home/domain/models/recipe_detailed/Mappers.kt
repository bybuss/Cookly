package bob.colbaskin.cookly.home.domain.models.recipe_detailed

import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedDto
import bob.colbaskin.cookly.home.presentation.recipe_detailed.DEFAULT_RECIPE_PORTIONS
import bob.colbaskin.cookly.home.presentation.recipe_detailed.RecipeCartIngredientUi
import java.util.Locale
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
                id = item.ingredient.id,
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

fun RecipeDetailed.toCartIngredientUiItems(portions: Int): List<RecipeCartIngredientUi> {
    return ingredients.map { ingredient ->
        val cartKey = ingredient.id.let { "ingredient_$it" }
        val calculatedQuantity = ingredient.count * portions / DEFAULT_RECIPE_PORTIONS
        RecipeCartIngredientUi(
            cartKey = cartKey,
            ingredientId = ingredient.id,
            title = ingredient.name,
            baseQuantity = ingredient.count.toDouble(),
            calculatedQuantity = calculatedQuantity.toDouble(),
            unitMeasurement = ingredient.unitOfMeasurement,
            isSelected = true
        )
    }
}

fun List<RecipeCartIngredientUi>.recalculateByPortions(portions: Int): List<RecipeCartIngredientUi> {
    return map { item ->
        item.copy(calculatedQuantity = item.baseQuantity * portions / DEFAULT_RECIPE_PORTIONS)
    }
}

fun Double.formatQuantity(): String {
    return if (this % 1.0 == 0.0) {
        toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", this).trimEnd('0').trimEnd('.')
    }
}
