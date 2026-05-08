package bob.colbaskin.cookly.create_recipe.data.models

import bob.colbaskin.cookly.create_recipe.presentation.CreateRecipeState

fun bob.colbaskin.cookly.create_recipe.domain.models.LocalImage.toDomain(): bob.colbaskin.cookly.create_recipe.domain.models.UploadImage {
    return _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.UploadImage(
        uri = uri,
        fileName = displayName,
        mimeType = mimeType
    )
}

fun IngredientSearchResponseDto.toDomain(): bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredient {
    return _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredient(
        ingredientId = id,
        title = title,
        unitMeasurement = defaultUnitMeasurement
    )
}

fun RecipeCategoryDto.toDomain(): bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategory {
    return _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategory(
        categoryId = id,
        title = title
    )
}

fun bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand.toDto(): CreateRecipeRequestDto {
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

fun CreateRecipeState.toCommand(): bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand {
    return _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand(
        title = title.trim(),
        description = description.trim(),
        estimatedTime = (estimatedHour * 60) + estimatedMinute,
        caloriesBy100Grams = caloriesBy100Grams.toInt(),
        mealTime = mealTimeType!!.apiValue,
        categories = categories.map {
            _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategoryCommand(
                categoryId = it.categoryId
            )
        },
        ingredients = ingredients.map {
            _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredientCommand(
                ingredientId = it.ingredientId,
                quantity = it.quantity.replace(",", ".").toDouble(),
                unitMeasurement = it.unitMeasurement.trim()
            )
        },
        spicyLevel = spicyLevel,
        difficultyLevel = difficultyLevel,
        steps = steps.map {
            _root_ide_package_.bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeStepCommand(
                number = it.number,
                title = it.title.trim(),
                description = it.description.trim(),
                image = it.image?.toDomain()
            )
        },
        mainPhoto = mainPhoto?.toDomain()
    )
}
