package bob.colbaskin.cookly.chat.domain.models

import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import java.time.Instant

data class ChatMessage(
    val id: String,
    val sender: ChatSender,
    val text: String,
    val recipes: List<RecipePreview> = emptyList(),
    val isTyping: Boolean = false,
    val createdAt: Instant = Instant.now()
)
