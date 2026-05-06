package bob.colbaskin.cookly.home.presentation.recipe_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.favourite.domain.FavoritesChangeNotifier
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import bob.colbaskin.cookly.home.data.models.recipe_detailed.recalculateByPortions
import bob.colbaskin.cookly.home.data.models.recipe_detailed.toCartIngredientUiItems
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import bob.colbaskin.cookly.shopping_cart.domain.ShoppingCartRepository
import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailedViewModel @Inject constructor(
    private val homeRecipeRepository: HomeRecipeRepository,
    private val shoppingCartRepository: ShoppingCartRepository,
    private val profileRepository: ProfileRepository,
    private val favoritesChangeNotifier: FavoritesChangeNotifier
) : ViewModel() {

    var state by mutableStateOf(RecipeDetailedState())
        private set

    init {
        observeLocalUser()
    }

    private var loadedRecipeId: Int? = null

    fun onAction(action: RecipeDetailedAction) {
        when (action) {
            RecipeDetailedAction.ToggleLike -> toggleLike()
            is RecipeDetailedAction.OnSheetStateChanged -> {
                state = state.copy(isSheetExpanded = action.isExpanded)
            }
            RecipeDetailedAction.ShowAddToCartSheet -> openAddToCartSheet()
            RecipeDetailedAction.HideAddToCartSheet -> {
                state = state.copy(isAddToCartSheetVisible = false)
            }
            RecipeDetailedAction.IncreasePortions -> {
                val newPortions = (state.portions + 1).coerceAtMost(99)
                state = state.copy(
                    portions = newPortions,
                    cartIngredients = state.cartIngredients.recalculateByPortions(newPortions)
                )
            }
            RecipeDetailedAction.DecreasePortions -> {
                val newPortions = (state.portions - 1).coerceAtLeast(1)
                state = state.copy(
                    portions = newPortions,
                    cartIngredients = state.cartIngredients.recalculateByPortions(newPortions)
                )
            }
            is RecipeDetailedAction.ToggleCartIngredient -> {
                state = state.copy(
                    cartIngredients = state.cartIngredients.map { item ->
                        if (item.cartKey == action.cartKey) {
                            item.copy(isSelected = !item.isSelected)
                        } else {
                            item
                        }
                    }
                )
            }
            RecipeDetailedAction.ConfirmAddSelectedIngredientsToCart -> {
                addSelectedIngredientsToCart()
            }
            RecipeDetailedAction.ConsumeAddToCartResult -> {
                state = state.copy(addToCartState = UiState.Idle)
            }
            RecipeDetailedAction.StartCook -> startCook()
            is RecipeDetailedAction.ChangeActiveStep -> changeActiveStep(action.cookingSessionId)
            RecipeDetailedAction.ShowModeratorReviewSheet -> {
                state = state.copy(isModeratorReviewSheetVisible = true)
            }
            RecipeDetailedAction.HideModeratorReviewSheet -> {
                state = state.copy(isModeratorReviewSheetVisible = false)
            }
            RecipeDetailedAction.PublishRecipe -> publishRecipe()
            RecipeDetailedAction.ShowRatingSheet -> {
                state = state.copy(
                    isRatingSheetVisible = true,
                    selectedRating = state.userRate ?: 0,
                    setRateState = UiState.Idle
                )
            }
            RecipeDetailedAction.HideRatingSheet -> {
                state = state.copy(isRatingSheetVisible = false)
            }
            is RecipeDetailedAction.ChangeRating -> {
                state = state.copy(selectedRating = action.rating)
            }
            RecipeDetailedAction.SubmitRating -> submitRating()
            RecipeDetailedAction.ConsumeRatingResult -> {
                state = state.copy(setRateState = UiState.Idle)
            }
            else -> Unit
        }
    }

    fun loadRecipe(recipeId: Int) {
        if (recipeId <= 0) {
            state = state.copy(
                recipeState = UiState.Error(
                    title = "Некорректный ID рецепта",
                    text = "Не удалось открыть рецепт."
                )
            )
            return
        }
        state = state.copy(id = recipeId)
        if (loadedRecipeId == recipeId && state.recipeState is UiState.Success) return
        loadedRecipeId = recipeId
        state = state.copy(recipeState = UiState.Loading)

        viewModelScope.launch {
            val result = homeRecipeRepository.getRecipeById(recipeId).toUiState()

            state = when (result) {
                is UiState.Success -> {
                    val recipe = result.data
                    state.copy(
                        recipeState = result,
                        isFavorite = recipe.isFavorite,
                        userRate = recipe.userRate,
                        selectedRating = recipe.userRate ?: 0
                    )
                }

                else -> {
                    state.copy(recipeState = result)
                }
            }
        }
    }

    private fun observeLocalUser() {
        viewModelScope.launch {
            profileRepository.observeUserPreferences().collectLatest { prefs ->
                state = state.copy(
                    email = prefs.email,
                    avatarUrl = prefs.avatarUrl
                )
            }
        }
    }

    private fun openAddToCartSheet() {
        val recipe = (state.recipeState as? UiState.Success)?.data ?: return

        state = state.copy(
            isAddToCartSheetVisible = true,
            portions = DEFAULT_RECIPE_PORTIONS,
            cartIngredients = recipe.toCartIngredientUiItems(
                portions = DEFAULT_RECIPE_PORTIONS
            ),
            addToCartState = UiState.Idle
        )
    }

    private fun addSelectedIngredientsToCart() {
        if (state.addToCartState is UiState.Loading) return

        val selectedIngredients = state.cartIngredients
            .filter { it.isSelected }
            .map { item ->
                CartIngredient(
                    cartKey = item.cartKey,
                    ingredientId = item.ingredientId,
                    title = item.title,
                    quantity = item.calculatedQuantity,
                    unitMeasurement = item.unitMeasurement,
                    sourceRecipeId = (state.recipeState as? UiState.Success)?.data?.id
                )
            }

        if (selectedIngredients.isEmpty()) {
            state = state.copy(
                addToCartState = UiState.Error(
                    title = "Ингредиенты не выбраны",
                    text = "Выберите хотя бы один ингредиент для добавления."
                )
            )
            return
        }

        state = state.copy(addToCartState = UiState.Loading)

        viewModelScope.launch {
            val result = shoppingCartRepository.addIngredients(selectedIngredients).toUiState()

            state = when (result) {
                is UiState.Success -> {
                    state.copy(
                        addToCartState = result,
                        isAddToCartSheetVisible = false
                    )
                }

                else -> {
                    state.copy(addToCartState = result)
                }
            }
        }
    }

    private fun startCook() {
        if (state.startCookingState is UiState.Loading) return

        state = state.copy(startCookingState = UiState.Loading)

        viewModelScope.launch {
            val cookingSessionId = homeRecipeRepository.startCookingSession(state.id)
            state = state.copy(startCookingState = cookingSessionId.toUiState())
        }
    }

    private fun changeActiveStep(cookingSessionId: Int, stepNumber: Int = 1) {
        viewModelScope.launch {
            homeRecipeRepository.changeActiveStep(
                cookingSessionId = cookingSessionId,
                stepNumber = stepNumber
            )
        }
    }

    private fun toggleLike() {
        viewModelScope.launch {
            val result = homeRecipeRepository.favorite(
                recipeId = state.id,
                isFavorite = !state.isFavorite
            ).toUiState()

            when (result) {
                is UiState.Success -> {
                    state = state.copy(isFavorite = !state.isFavorite)
                    favoritesChangeNotifier.notifyChanged()
                }
                else -> Unit
            }
        }
    }

    private fun submitRating() {
        if (state.setRateState is UiState.Loading) return
        val rating = state.selectedRating

        state = state.copy(setRateState = UiState.Loading)

        viewModelScope.launch {
            val result = homeRecipeRepository.setRate(recipeId = state.id, rating = rating).toUiState()

            state = when (result) {
                is UiState.Success -> {
                    val currentRecipe = (state.recipeState as? UiState.Success)?.data
                    state.copy(
                        setRateState = result,
                        isRatingSheetVisible = false,
                        userRate = rating,
                        recipeState = if (currentRecipe != null) {
                            UiState.Success(currentRecipe.copy(userRate = rating))
                        } else {
                            state.recipeState
                        }
                    )
                }
                else -> state.copy(setRateState = result)
            }
        }
    }

    private fun publishRecipe() {
        viewModelScope.launch {
            val pubRecipeRequestId
                = (state.recipeState as? UiState.Success)?.data?.pubRecipeRequestId ?: return@launch
            state = state.copy(publicateRecipeState = UiState.Loading)
            val result = homeRecipeRepository.approveRecipeRequest(
                pubRecipeRequestId = pubRecipeRequestId
            ).toUiState()
            state = state.copy(publicateRecipeState = result)
        }
    }
}
