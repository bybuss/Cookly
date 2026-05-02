package bob.colbaskin.cookly.home.data.models.main

import bob.colbaskin.cookly.home.domain.models.main.ActiveCookingSession
import bob.colbaskin.cookly.home.domain.models.main.FeedPage
import bob.colbaskin.cookly.home.domain.models.main.FeedRecipe
import bob.colbaskin.cookly.home.domain.models.meal.MealFeedItem

fun FeedResponseDto.toDomain(): FeedPage {
    return FeedPage(
        recipes = recipes.map { it.toDomain() },
        lastRecipeScore = lastRecipeScore,
        lastRecipeId = lastRecipeId,
        paginationKey = paginationKey
    )
}

fun FeedRecipeDto.toDomain(): FeedRecipe {
    return FeedRecipe(
        id = id,
        title = title,
        estimatedTime = estimatedTime,
        caloriesBy100Grams = caloriesBy100Grams,
        mealTime = mealTime,
        ratingSum = ratingSum,
        ratingCount = ratingCount,
        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,
        imageUrl = imageUrl
    )
}

fun FeedRecipe.toMealFeedItem(): MealFeedItem {
    return MealFeedItem(
        id = id,
        imageUrl = imageUrl,
        title = title,
        description = "$estimatedTime мин. $caloriesBy100Grams калорий"
    )
}

fun ActiveSessionDto.toDomain(): ActiveCookingSession {
    return ActiveCookingSession(
        sessionId = session.id,
        recipeId = recipeId,
        recipeTitle = recipeTitle,
        recipeImageUrl = recipeImageUrl,
        currentStepNumber = session.currentStep,
        stepTitle = stepTitle
    )
}
