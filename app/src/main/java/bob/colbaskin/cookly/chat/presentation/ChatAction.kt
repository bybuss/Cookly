package bob.colbaskin.cookly.chat.presentation

sealed interface ChatAction {

    data class ChangeInputText(val value: String): ChatAction

    data object SendMessage: ChatAction

    data class OpenRecipe(val recipeId: Int): ChatAction
}
