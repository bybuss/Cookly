package bob.colbaskin.cookly.shopping_cart.data.models

import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient

fun CartIngredientEntity.toDomain(): CartIngredient {
    return CartIngredient(
        cartKey = cartKey,
        ingredientId = ingredientId,
        title = title,
        quantity = quantity,
        unitMeasurement = unitMeasurement,
        sourceRecipeId = sourceRecipeId
    )
}

fun CartIngredient.toEntity(
    createdAtMillis: Long = System.currentTimeMillis()
): CartIngredientEntity {
    return CartIngredientEntity(
        cartKey = cartKey,
        ingredientId = ingredientId,
        title = title,
        quantity = quantity,
        unitMeasurement = unitMeasurement,
        sourceRecipeId = sourceRecipeId,
        createdAtMillis = createdAtMillis
    )
}
