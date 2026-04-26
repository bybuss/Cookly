package bob.colbaskin.cookly.create_recipe.domain.models

import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeIngredientDto
import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeRequestDto
import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeStepDto

fun LocalImage.toDomain(): UploadImage {
    return UploadImage(
        uri = uri,
        fileName = displayName,
        mimeType = mimeType
    )
}

fun CreateRecipeCommand.toDto(): CreateRecipeRequestDto {
    return CreateRecipeRequestDto(
        title = title,
        description = description,
        estimatedTime = estimatedTime,
        caloriesBy100Grams = caloriesBy100Grams,
        mealTime = mealTime,
        steps = steps.map {
            CreateRecipeStepDto(
                title = it.title,
                description = it.description,
                number = it.number
            )
        },
        recipeIngredients = ingredients.map {
            CreateRecipeIngredientDto(
                ingredientId = it.ingredientId,
                unitMeasurement = it.unitMeasurement,
                quantity = it.quantity
            )
        },
        recipeCategoriesIds = categories.map { it.categoryId }
    )
}
