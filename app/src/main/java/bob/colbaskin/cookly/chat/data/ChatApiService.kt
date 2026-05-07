package bob.colbaskin.cookly.chat.data

import bob.colbaskin.cookly.chat.data.models.ChatBodyDto
import bob.colbaskin.cookly.chat.data.models.ChatResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {

    @POST("/chat")
    suspend fun sendMessage(
        @Body body: ChatBodyDto
    ): ChatResponseDto
}
