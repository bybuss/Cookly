package bob.colbaskin.cookly.chat.domain.models

import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview

data class ChatResponse(
    val isFoodRelated: Boolean,
    val textResponse: String,
    val recipes: List<RecipePreview>
)
