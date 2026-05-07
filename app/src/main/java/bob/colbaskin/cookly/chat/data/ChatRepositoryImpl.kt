package bob.colbaskin.cookly.chat.data

import android.content.Context
import bob.colbaskin.cookly.chat.data.models.ChatBodyDto
import bob.colbaskin.cookly.chat.data.models.toDomain
import bob.colbaskin.cookly.chat.domain.ChatRepository
import bob.colbaskin.cookly.chat.domain.models.ChatResponse
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.utils.safeApiCall
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ChatApiService
) : ChatRepository {

    override suspend fun sendMessage(message: String): ApiResult<ChatResponse> {
        return safeApiCall(
            apiCall = { apiService.sendMessage(body = ChatBodyDto(message = message)) },
            successHandler = { response -> response.toDomain() },
            context = context
        )
    }
}
