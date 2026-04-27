package bob.colbaskin.cookly.create_recipe.presentation

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategory
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredient
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeStep
import bob.colbaskin.cookly.create_recipe.domain.models.LocalImage
import bob.colbaskin.cookly.home.domain.models.MealTimeType

data class CreateRecipeState(
    val title: String = "",
    val description: String = "",
    val caloriesBy100Grams: String = "",
    val mealTimeType: MealTimeType? = null,
    val estimatedHour: Int = 0,
    val estimatedMinute: Int = 0,

    val categories: List<CreateRecipeCategory> = emptyList(),
    val availableCategories: List<CreateRecipeCategory> = emptyList(),
    val isCategoriesLoading: Boolean = false,
    val categoriesError: String? = null,

    val ingredients: List<CreateRecipeIngredient> = emptyList(),
    val editingIngredient: CreateRecipeIngredient? = null,
    val ingredientSearchQuery: String = "",
    val ingredientSearchResults: List<CreateRecipeIngredient> = emptyList(),
    val isIngredientSearchLoading: Boolean = false,
    val ingredientSearchError: String? = null,

    val steps: List<CreateRecipeStep> = listOf(
        CreateRecipeStep(localId = 1L, number = 1)
    ),

    val mainPhoto: LocalImage? = null,

    val isTimePickerVisible: Boolean = false,
    val isCategorySheetVisible: Boolean = false,
    val isIngredientSheetVisible: Boolean = false,

    val submitState: UiState<Int>? = null
) {
    val isSubmitting: Boolean
        get() = submitState is UiState.Loading

    fun estimatedTimeLabel(): String {
        if (estimatedHour == 0 && estimatedMinute == 0) return "Выберите время"
        return buildString {
            if (estimatedHour > 0) append("$estimatedHour ч ")
            if (estimatedMinute > 0) append("$estimatedMinute мин")
        }.trim()
    }
}
