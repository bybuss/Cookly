package bob.colbaskin.cookly.chat.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatBodyDto (
    val message: String
)