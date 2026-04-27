package bob.colbaskin.cookly.create_recipe.domain.models

import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeIngredientDto
import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeRequestDto
import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeStepDto
import bob.colbaskin.cookly.create_recipe.data.models.IngredientSearchResponseDto
import bob.colbaskin.cookly.create_recipe.data.models.RecipeCategoryDto
import bob.colbaskin.cookly.create_recipe.presentation.CreateRecipeState

fun LocalImage.toDomain(): UploadImage {
    return UploadImage(
        uri = uri,
        fileName = displayName,
        mimeType = mimeType
    )
}

fun IngredientSearchResponseDto.toDomain(): CreateRecipeIngredient {
    return CreateRecipeIngredient(
        ingredientId = id,
        title = title,
        unitMeasurement = defaultUnitMeasurement
    )
}

fun RecipeCategoryDto.toDomain(): CreateRecipeCategory {
    return CreateRecipeCategory(
        categoryId = id,
        title = title
    )
}

fun CreateRecipeCommand.toDto(): CreateRecipeRequestDto {
    return CreateRecipeRequestDto(
        title = title,
        description = description,
        estimatedTime = estimatedTime,
        caloriesBy100Grams = caloriesBy100Grams,
        mealTime = mealTime,
        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,
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

fun CreateRecipeState.toCommand(): CreateRecipeCommand {
    return CreateRecipeCommand(
        title = title.trim(),
        description = description.trim(),
        estimatedTime = (estimatedHour * 60) + estimatedMinute,
        caloriesBy100Grams = caloriesBy100Grams.toIntOrNull(),
        mealTime = mealTimeType?.apiValue,
        categories = categories.map {
            CreateRecipeCategoryCommand(categoryId = it.categoryId)
        },
        ingredients = ingredients.map {
            CreateRecipeIngredientCommand(
                ingredientId = it.ingredientId,
                quantity = it.quantity.replace(",", ".").toDouble(),
                unitMeasurement = it.unitMeasurement.trim()
            )
        },
        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,
        steps = steps.map {
            CreateRecipeStepCommand(
                number = it.number,
                title = it.title.trim(),
                description = it.description.trim(),
                image = it.image?.toDomain()
            )
        },
        mainPhoto = mainPhoto?.toDomain()
    )
}
