package bob.colbaskin.cookly.create_recipe.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategoryCommand
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredientCommand
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeStep
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeStepCommand
import bob.colbaskin.cookly.create_recipe.domain.models.toDomain
import bob.colbaskin.cookly.create_recipe.domain.CreateRecipeRepository

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val repository: CreateRecipeRepository
) : ViewModel() {

    var state by mutableStateOf(CreateRecipeState())
        private set

    private var nextStepLocalId: Long = 2L

    fun onAction(action: CreateRecipeAction) {
        when (action) {
            CreateRecipeAction.Back -> Unit

            is CreateRecipeAction.UpdateTitle -> {
                state = state.copy(title = action.value)
            }

            is CreateRecipeAction.UpdateDescription -> {
                state = state.copy(description = action.value)
            }

            is CreateRecipeAction.UpdateCaloriesBy100Grams -> {
                state = state.copy(
                    caloriesBy100Grams = action.value.filter { it.isDigit() }
                )
            }

            is CreateRecipeAction.UpdateMealTime -> {
                state = state.copy(mealTime = action.value)
            }

            CreateRecipeAction.OpenTimePicker -> {
                state = state.copy(isTimePickerVisible = true)
            }

            CreateRecipeAction.DismissTimePicker -> {
                state = state.copy(isTimePickerVisible = false)
            }

            is CreateRecipeAction.ConfirmTime -> {
                state = state.copy(
                    estimatedHour = action.hour,
                    estimatedMinute = action.minute,
                    isTimePickerVisible = false
                )
            }

            CreateRecipeAction.ShowCategorySheet -> {
                state = state.copy(isCategorySheetVisible = true)
            }

            CreateRecipeAction.HideCategorySheet -> {
                state = state.copy(isCategorySheetVisible = false)
            }

            is CreateRecipeAction.AddCategory -> {
                val updated = state.categories
                    .filterNot { it.categoryId == action.category.categoryId } + action.category

                state = state.copy(
                    categories = updated,
                    isCategorySheetVisible = false
                )
            }

            is CreateRecipeAction.RemoveCategory -> {
                state = state.copy(
                    categories = state.categories.filterNot { it.categoryId == action.categoryId }
                )
            }

            CreateRecipeAction.ShowIngredientSheet -> {
                state = state.copy(isIngredientSheetVisible = true)
            }

            CreateRecipeAction.HideIngredientSheet -> {
                state = state.copy(isIngredientSheetVisible = false)
            }

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

            is CreateRecipeAction.SetMainPhoto -> {
                state = state.copy(mainPhoto = action.image)
            }

            CreateRecipeAction.RemoveMainPhoto -> {
                state = state.copy(mainPhoto = null)
            }

            CreateRecipeAction.Submit -> submit()

            CreateRecipeAction.ConsumeSuccess -> {
                state = state.copy(submitState = null)
            }

            CreateRecipeAction.DismissError -> {
                if (state.submitState is UiState.Error) {
                    state = state.copy(submitState = null)
                }
            }
        }
    }

    private fun submit() {
        if (state.isSubmitting) return

        state = state.copy(submitState = UiState.Loading)

        viewModelScope.launch {
            val result = repository.submitRecipe(state.toCommand()).toUiState()
            state = state.copy(submitState = result)
        }
    }

    private fun CreateRecipeState.toCommand(): CreateRecipeCommand {
        return CreateRecipeCommand(
            title = title.trim(),
            description = description.trim(),
            estimatedTime = estimatedTimeToApiValue(
                hour = estimatedHour,
                minute = estimatedMinute
            ),
            caloriesBy100Grams = caloriesBy100Grams.toIntOrNull(),
            mealTime = mealTime.trim().takeIf { it.isNotBlank() },
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
            steps = steps.map {
                CreateRecipeStepCommand(
                    number = it.number,
                    title = it.title.trim(),
                    description = it.description,
                    image = it.image?.toDomain()
                )
            },
            mainPhoto = mainPhoto?.toDomain()
        )
    }

    private fun estimatedTimeToApiValue(hour: Int, minute: Int): Int {
        return hour * 60 + minute
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
