package bob.colbaskin.cookly.shopping_cart.presentation

import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient

sealed interface ShoppingCartAction {
    data class DeleteIngredient(val cartKey: String): ShoppingCartAction
    data object ClearCart: ShoppingCartAction

    data class OpenEditSheet(val ingredient: CartIngredient): ShoppingCartAction
    data object CloseEditSheet: ShoppingCartAction
    data class UpdateEditingQuantity(val value: String): ShoppingCartAction
    data object SaveEditingIngredient: ShoppingCartAction

    data object DismissError: ShoppingCartAction
}
