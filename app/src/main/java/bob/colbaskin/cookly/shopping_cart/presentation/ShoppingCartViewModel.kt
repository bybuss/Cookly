package bob.colbaskin.cookly.shopping_cart.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.formatQuantity
import bob.colbaskin.cookly.shopping_cart.domain.ShoppingCartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val repository: ShoppingCartRepository
) : ViewModel() {

    var state by mutableStateOf(ShoppingCartState())
        private set

    init {
        observeCart()
    }

    fun onAction(action: ShoppingCartAction) {
        when (action) {
            is ShoppingCartAction.DeleteIngredient -> deleteIngredient(action.cartKey)
            ShoppingCartAction.ClearCart -> clearCart()
            is ShoppingCartAction.OpenEditSheet -> {
                state = state.copy(
                    editIngredient = action.ingredient,
                    editQuantity = action.ingredient.quantity.formatQuantity()
                )
            }
            ShoppingCartAction.CloseEditSheet -> {
                state = state.copy(
                    editIngredient = null,
                    editQuantity = ""
                )
            }
            is ShoppingCartAction.UpdateEditingQuantity -> {
                state = state.copy(
                    editQuantity = action.value
                        .replace(",", ".")
                        .filter { it.isDigit() || it == '.' }
                )
            }
            ShoppingCartAction.SaveEditingIngredient -> saveEditingIngredient()
            ShoppingCartAction.DismissError -> state = state.copy(actionState = UiState.Idle)
            else -> Unit
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            state = state.copy(cartState = UiState.Loading)

            repository.observeCartIngredients()
                .collect { ingredients ->
                    state = state.copy(
                        cartState = UiState.Success(ingredients)
                    )
                }
        }
    }

    private fun deleteIngredient(cartKey: String) {
        viewModelScope.launch {
            state = state.copy(actionState = UiState.Loading)
            val result = repository.deleteIngredient(cartKey).toUiState()
            state = state.copy(actionState = result)
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            state = state.copy(actionState = UiState.Loading)
            val result = repository.clearCart().toUiState()
            state = state.copy(actionState = result)
        }
    }

    private fun saveEditingIngredient() {
        val ingredient = state.editIngredient ?: return
        val quantity = state.editQuantity.toDoubleOrNull()
        if (quantity == null || quantity <= 0.0) {
            state = state.copy(
                actionState = UiState.Error(
                    title = "Некорректное количество",
                    text = "Введите число больше нуля."
                )
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(actionState = UiState.Loading)
            val result = repository.updateIngredientQuantity(
                cartKey = ingredient.cartKey,
                quantity = quantity
            ).toUiState()
            state = when (result) {
                is UiState.Success -> {
                    state.copy(
                        actionState = result,
                        editIngredient = null,
                        editQuantity = ""
                    )
                }
                else -> state.copy(actionState = result)
            }
        }
    }
}
