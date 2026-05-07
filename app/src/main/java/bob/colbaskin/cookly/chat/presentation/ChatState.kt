package bob.colbaskin.cookly.chat.presentation

import bob.colbaskin.cookly.chat.domain.models.ChatMessage

data class ChatState(
    val inputText: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false
)
