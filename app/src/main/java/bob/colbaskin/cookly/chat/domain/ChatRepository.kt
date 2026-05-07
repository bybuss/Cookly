package bob.colbaskin.cookly.chat.domain

import bob.colbaskin.cookly.chat.domain.models.ChatResponse
import bob.colbaskin.cookly.common.ApiResult

interface ChatRepository {

    suspend fun sendMessage(message: String): ApiResult<ChatResponse>
}
