package bob.colbaskin.cookly.common.recipe_preview.data.models

import bob.colbaskin.cookly.common.recipe_preview.domain.models.MealTimeType
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview

fun RecipePreviewDto.toDomain(): RecipePreview {
    return RecipePreview(
        id = id,
        title = title,
        estimatedTime = estimatedTime,
        caloriesBy100Grams = caloriesBy100Grams,
        mealTime = mealTime.toMealTimeType(),
        ratingSum = ratingSum,
        ratingCount = ratingCount,
        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,
        imageUrl = imageUrl
    )
}

fun String.toMealTimeType(): MealTimeType {
    return when (this) {
        "breakfast" -> MealTimeType.BREAKFAST
        "lunch" -> MealTimeType.LUNCH
        "supper" -> MealTimeType.SUPPER
        else -> MealTimeType.BREAKFAST
    }
}