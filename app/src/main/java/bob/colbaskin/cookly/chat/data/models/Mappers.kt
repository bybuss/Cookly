package bob.colbaskin.cookly.chat.data.models

import bob.colbaskin.cookly.chat.domain.models.ChatResponse
import bob.colbaskin.cookly.common.recipe_preview.data.models.toDomain

fun ChatResponseDto.toDomain(): ChatResponse {
    return ChatResponse(
        isFoodRelated = isFoodRelated,
        textResponse = textResponse,
        recipes = recipes.map { it.toDomain() }
    )
}
