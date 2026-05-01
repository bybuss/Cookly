package bob.colbaskin.cookly.home.data.models.recipe_detailed

import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedCategoryDto
import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedDto
import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedResponseDto
import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedStepDto
import bob.colbaskin.cookly.home.presentation.recipe_detailed.DEFAULT_RECIPE_PORTIONS
import bob.colbaskin.cookly.home.presentation.recipe_detailed.RecipeCartIngredientUi
import java.util.Locale
import kotlin.math.roundToInt

fun RecipeDetailedResponseDto.toDomain(): bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed {
    return _root_ide_package_.bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed(
        id = recipe.id,
        title = recipe.title,
        description = recipe.description,
        estimatedTime = recipe.estimatedTime,
        caloriesBy100Grams = recipe.caloriesBy100Grams.roundToInt(),
        mealTime = recipe.mealTime,
        rating = calculateRating(
            ratingSum = recipe.ratingSum,
            ratingCount = recipe.ratingCount
        ),
        ratingCount = recipe.ratingCount,
        spicyLevel = recipe.spicyLevel,
        difficultyLevel = recipe.difficultyLevel,
        imageUrl = recipe.imageUrl,
        ingredients = recipe.recipeIngredients.map { item ->
            _root_ide_package_.bob.colbaskin.cookly.home.domain.models.recipe_detailed.Ingredient(
                id = item.ingredient.id,
                name = item.ingredient.title,
                count = item.quantity.toIngredientCount(),
                unitOfMeasurement = item.unitMeasurement
            )
        },
        steps = recipe.steps.map { it.toDomain() },
        categories = recipe.recipeCategories.map { it.toDomain() },
        isFavorite = isFavorite,
        userRate = userRate
    )
}

fun RecipeDetailedStepDto.toDomain(): bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeStep {
    return _root_ide_package_.bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeStep(
        id = id,
        title = title,
        description = description,
        number = number,
        imageUrl = imageUrl
    )
}

fun RecipeDetailedCategoryDto.toDomain(): bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeCategory {
    return _root_ide_package_.bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeCategory(
        id = id,
        title = title
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

fun bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed.toCartIngredientUiItems(portions: Int): List<RecipeCartIngredientUi> {
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
