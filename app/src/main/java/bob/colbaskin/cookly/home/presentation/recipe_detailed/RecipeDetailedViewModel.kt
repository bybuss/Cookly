package bob.colbaskin.cookly.home.presentation.recipe_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import bob.colbaskin.cookly.home.domain.models.old.Allergen
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.Ingredient
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailedViewModel @Inject constructor(
    private val repository: HomeRecipeRepository
) : ViewModel() {

    var state by mutableStateOf(RecipeDetailedState())
        private set

    private var loadedRecipeId: Int? = null

    fun onAction(action: RecipeDetailedAction) {
        when (action) {
            is RecipeDetailedAction.OnSheetStateChanged -> {
                state = state.copy(isSheetExpanded = action.isExpanded)
            }
            RecipeDetailedAction.ToggleLike -> {
                state = state.copy(isRecipeLiked = !state.isRecipeLiked)
            }
            else -> Unit
        }
    }

//    fun loadRecipe(recipeId: Int) {
//        if (recipeId <= 0) {
//            state = state.copy(
//                recipeState = UiState.Error(
//                    title = "Некорректный ID рецепта",
//                    text = "Не удалось открыть рецепт."
//                )
//            )
//            return
//        }
//        if (loadedRecipeId == recipeId && state.recipeState is UiState.Success) return
//        loadedRecipeId = recipeId
//        state = state.copy(recipeState = UiState.Loading)
//        viewModelScope.launch {
//            val result = repository.getRecipeById(recipeId).toUiState()
//            state = state.copy(recipeState = result)
//        }
//    }

    fun loadRecipe(recipeId: Int) {
        state = state.copy(recipeState = UiState.Loading)

        viewModelScope.launch {
            kotlinx.coroutines.delay(500)

            state = state.copy(
                recipeState = UiState.Success(
                    RecipeDetailed(
                        id = recipeId,
                        title = "Паста с курицей и томатами",
                        description = "Нежная паста с курицей, томатным соусом и базиликом. Подходит для быстрого обеда или ужина.",
                        estimatedTime = 35,
                        caloriesBy100Grams = 245,
                        mealTime = "lunch",
                        rating = 4.7,
                        ratingCount = 128,
                        spicyLevel = 2,
                        difficultyLevel = 3,
                        imageUrl = "https://bidlo.taild3ccfe.ts.net/recipes/62.webp",
                        ingredients = listOf(
                            Ingredient(
                                name = "Паста",
                                count = 200,
                                unitOfMeasurement = "г"
                            ),
                            Ingredient(
                                name = "Куриное филе",
                                count = 250,
                                unitOfMeasurement = "г"
                            ),
                            Ingredient(
                                name = "Томаты",
                                count = 2,
                                unitOfMeasurement = "шт"
                            ),
                            Ingredient(
                                name = "Сыр",
                                count = 50,
                                unitOfMeasurement = "г"
                            )
                        ),
                        categories = listOf(
                            "Основные блюда",
                            "Паста и лапша"
                        )
                    )
                )
            )
        }
    }
}
