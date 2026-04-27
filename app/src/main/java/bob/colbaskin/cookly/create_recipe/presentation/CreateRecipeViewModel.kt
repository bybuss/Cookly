package bob.colbaskin.cookly.create_recipe.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.create_recipe.domain.CreateRecipeRepository
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeStep
import bob.colbaskin.cookly.create_recipe.domain.models.toCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val repository: CreateRecipeRepository
) : ViewModel() {

    var state by mutableStateOf(CreateRecipeState())
        private set

    private var nextStepLocalId: Long = 2L
    private var ingredientSearchJob: Job? = null

    fun onAction(action: CreateRecipeAction) {
        when (action) {
            CreateRecipeAction.Back -> Unit

            is CreateRecipeAction.UpdateTitle -> { state = state.copy(title = action.value) }

            is CreateRecipeAction.UpdateDescription -> {
                state = state.copy(description = action.value)
            }

            is CreateRecipeAction.UpdateCaloriesBy100Grams -> {
                state = state.copy(
                    caloriesBy100Grams = action.value.filter { it.isDigit() }
                )
            }

            is CreateRecipeAction.SelectMealTime -> {
                state = state.copy(mealTimeType = action.mealTimeType)
            }

            CreateRecipeAction.OpenTimePicker -> { state = state.copy(isTimePickerVisible = true) }

            CreateRecipeAction.DismissTimePicker -> { state = state.copy(isTimePickerVisible = false) }

            is CreateRecipeAction.ConfirmTime -> {
                state = state.copy(
                    estimatedHour = action.hour,
                    estimatedMinute = action.minute,
                    isTimePickerVisible = false
                )
            }

            CreateRecipeAction.ShowCategorySheet -> {
                state = state.copy(isCategorySheetVisible = true)
                loadCategoriesIfNeeded()
            }

            CreateRecipeAction.HideCategorySheet -> {
                state = state.copy(isCategorySheetVisible = false)
            }

            is CreateRecipeAction.SetCategories -> {
                state = state.copy(
                    categories = action.categories,
                    isCategorySheetVisible = false
                )
            }

            is CreateRecipeAction.RemoveCategory -> {
                state = state.copy(
                    categories = state.categories.filterNot {
                        it.categoryId == action.categoryId
                    }
                )
            }

            CreateRecipeAction.ShowIngredientSheet -> {
                state = state.copy(
                    isIngredientSheetVisible = true,
                    ingredientSearchQuery = "",
                    ingredientSearchResults = emptyList(),
                    ingredientSearchError = null
                )
            }

            CreateRecipeAction.HideIngredientSheet -> {
                state = state.copy(isIngredientSheetVisible = false)
            }

            is CreateRecipeAction.SearchIngredients -> { searchIngredients(action.query) }

            is CreateRecipeAction.AddIngredient -> {
                val updated = state.ingredients
                    .filterNot { it.ingredientId == action.ingredient.ingredientId } +
                        action.ingredient
                state = state.copy(
                    ingredients = updated,
                    isIngredientSheetVisible = false
                )
            }

            is CreateRecipeAction.RemoveIngredient -> {
                state = state.copy(
                    ingredients = state.ingredients.filterNot {
                        it.ingredientId == action.ingredientId
                    }
                )
            }

            is CreateRecipeAction.MoveIngredient -> {
                state = state.copy(
                    ingredients = state.ingredients.move(action.fromIndex, action.toIndex)
                )
            }

            CreateRecipeAction.AddStep -> {
                state = state.copy(
                    steps = state.steps + CreateRecipeStep(
                        localId = nextStepLocalId++,
                        number = state.steps.size + 1
                    )
                )
            }

            is CreateRecipeAction.UpdateStepTitle -> {
                state = state.copy(
                    steps = state.steps.map { step ->
                        if (step.localId == action.stepLocalId) {
                            step.copy(title = action.value)
                        } else {
                            step
                        }
                    }
                )
            }

            is CreateRecipeAction.UpdateStepDescription -> {
                state = state.copy(
                    steps = state.steps.map { step ->
                        if (step.localId == action.stepLocalId) {
                            step.copy(description = action.value)
                        } else {
                            step
                        }
                    }
                )
            }

            is CreateRecipeAction.SetStepPhoto -> {
                state = state.copy(
                    steps = state.steps.map { step ->
                        if (step.localId == action.stepLocalId) {
                            step.copy(image = action.image)
                        } else {
                            step
                        }
                    }
                )
            }

            is CreateRecipeAction.RemoveStep -> {
                if (state.steps.size == 1) return
                state = state.copy(
                    steps = state.steps
                        .filterNot { it.localId == action.stepLocalId }
                        .reNumber()
                )
            }

            is CreateRecipeAction.MoveStep -> {
                state = state.copy(
                    steps = state.steps
                        .move(action.fromIndex, action.toIndex)
                        .reNumber()
                )
            }

            is CreateRecipeAction.SetMainPhoto -> { state = state.copy(mainPhoto = action.image) }

            CreateRecipeAction.RemoveMainPhoto -> { state = state.copy(mainPhoto = null) }

            CreateRecipeAction.Submit -> submit()

            CreateRecipeAction.ConsumeSuccess -> { state = state.copy(submitState = null) }

            CreateRecipeAction.DismissError -> {
                if (state.submitState is UiState.Error) {
                    state = state.copy(submitState = null)
                }
            }
        }
    }

    private fun loadCategoriesIfNeeded() {
        if (state.availableCategories.isNotEmpty() || state.isCategoriesLoading) return

        state = state.copy(
            isCategoriesLoading = true,
            categoriesError = null
        )

        viewModelScope.launch {
            state = when (val result = repository.getRecipeCategories()) {
                is ApiResult.Success -> {
                    state.copy(
                        availableCategories = result.data,
                        isCategoriesLoading = false
                    )
                }

                is ApiResult.Error -> {
                    state.copy(
                        isCategoriesLoading = false,
                        categoriesError = result.title
                    )
                }
            }
        }
    }

    private fun searchIngredients(query: String) {
        state = state.copy(ingredientSearchQuery = query)

        ingredientSearchJob?.cancel()

        if (query.isBlank()) {
            state = state.copy(
                ingredientSearchResults = emptyList(),
                isIngredientSearchLoading = false,
                ingredientSearchError = null
            )
            return
        }

        ingredientSearchJob = viewModelScope.launch {
            delay(350)

            state = state.copy(
                isIngredientSearchLoading = true,
                ingredientSearchError = null
            )

            when (val result = repository.searchIngredients(query.trim())) {
                is ApiResult.Success -> {
                    state = state.copy(
                        ingredientSearchResults = result.data,
                        isIngredientSearchLoading = false
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        ingredientSearchResults = emptyList(),
                        isIngredientSearchLoading = false,
                        ingredientSearchError = result.title
                    )
                }
            }
        }
    }

    private fun submit() {
        if (state.isSubmitting) return

        val validationError = validateBeforeSubmit()
        if (validationError != null) {
            state = state.copy(
                submitState = UiState.Error(
                    title = validationError,
                    text = validationError
                )
            )
            return
        }

        state = state.copy(submitState = UiState.Loading)

        viewModelScope.launch {
            val result = repository.submitRecipe(state.toCommand()).toUiState()
            state = state.copy(submitState = result)
        }
    }

    private fun validateBeforeSubmit(): String? {
        val calories = state.caloriesBy100Grams.toIntOrNull()

        return when {
            state.title.isBlank() -> "Укажите название рецепта."
            state.description.isBlank() -> "Укажите описание рецепта."
            state.estimatedHour == 0 && state.estimatedMinute == 0 -> "Укажите время приготовления."
            state.mealTimeType == null -> "Выберите тип блюда."
            state.caloriesBy100Grams.isNotBlank() && calories == null -> {
                "Укажите корректную калорийность."
            }
            calories != null && calories !in 0..9999 -> {
                "Калорийность на 100 грамм должна быть от 0 до 9999."
            }
            state.ingredients.isEmpty() -> "Добавьте хотя бы один ингредиент."
            state.ingredients.any {
                val quantity = it.quantity.replace(",", ".").toDoubleOrNull()
                quantity == null || quantity <= 0.0 || quantity > 100_000.0
            } -> {
                "Калорийность ингредиента должна быть больше не больше 100000 и неменьше 0."
            }
            state.steps.any { it.title.isBlank() || it.description.isBlank() } -> {
                "Заполните заголовок и описание каждого шага."
            }
            else -> null
        }
    }
}

private fun <T> List<T>.move(fromIndex: Int, toIndex: Int): List<T> {
    if (fromIndex == toIndex) return this
    if (fromIndex !in indices || toIndex !in indices) return this

    val mutable = toMutableList()
    val item = mutable.removeAt(fromIndex)
    mutable.add(toIndex, item)

    return mutable.toList()
}

private fun List<CreateRecipeStep>.reNumber(): List<CreateRecipeStep> {
    return mapIndexed { index, item ->
        item.copy(number = index + 1)
    }
}
