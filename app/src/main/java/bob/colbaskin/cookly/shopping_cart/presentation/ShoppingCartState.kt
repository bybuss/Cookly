package bob.colbaskin.cookly.shopping_cart.presentation

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient

data class ShoppingCartState(
    val cartState: UiState<List<CartIngredient>> = UiState.Idle,
    val editIngredient: CartIngredient? = null,
    val editQuantity: String = "",
    val actionState: UiState<Unit> = UiState.Idle
)
