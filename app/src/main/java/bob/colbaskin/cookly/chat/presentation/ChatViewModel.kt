package bob.colbaskin.cookly.chat.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.chat.domain.ChatRepository
import bob.colbaskin.cookly.chat.domain.models.ChatMessage
import bob.colbaskin.cookly.chat.domain.models.ChatSender
import bob.colbaskin.cookly.common.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    var state by mutableStateOf(
        ChatState(
            messages = listOf(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = ChatSender.Chef,
                    text = "Привет! Я ИИ-шеф. Напишите, что хотите приготовить, а я подберу подходящие рецепты 👨‍🍳",
                    createdAt = Instant.now()
                )
            )
        )
    )
        private set

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.ChangeInputText -> {
                state = state.copy(inputText = action.value)
            }

            ChatAction.SendMessage -> {
                sendMessage()
            }

            is ChatAction.OpenRecipe -> Unit
        }
    }

    private fun sendMessage() {
        val messageText = state.inputText.trim()

        if (messageText.isBlank()) return
        if (state.isSending) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = ChatSender.User,
            text = messageText,
            createdAt = Instant.now()
        )

        val typingMessage = ChatMessage(
            id = TYPING_MESSAGE_ID,
            sender = ChatSender.Chef,
            text = "Печатает...",
            isTyping = true,
            createdAt = Instant.now()
        )

        state = state.copy(
            inputText = "",
            isSending = true,
            messages = state.messages + userMessage + typingMessage
        )

        viewModelScope.launch {
            val result = repository.sendMessage(messageText)

            state = when (result) {
                is ApiResult.Success -> {
                    val chefMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        sender = ChatSender.Chef,
                        text = result.data.textResponse,
                        recipes = result.data.recipes,
                        isTyping = false,
                        createdAt = Instant.now()
                    )

                    state.copy(
                        isSending = false,
                        messages = state.messages.replaceTypingMessage(chefMessage)
                    )
                }

                is ApiResult.Error -> {
                    val errorMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        sender = ChatSender.Chef,
                        text = result.title.ifBlank {
                            "Не удалось получить ответ. Попробуйте еще раз."
                        },
                        isTyping = false,
                        createdAt = Instant.now()
                    )

                    state.copy(
                        isSending = false,
                        messages = state.messages.replaceTypingMessage(errorMessage)
                    )
                }
            }
        }
    }

    private fun List<ChatMessage>.replaceTypingMessage(
        newMessage: ChatMessage
    ): List<ChatMessage> {
        return map { message ->
            if (message.id == TYPING_MESSAGE_ID) newMessage else message
        }
    }

    private companion object {
        const val TYPING_MESSAGE_ID = "typing_message"
    }
}
