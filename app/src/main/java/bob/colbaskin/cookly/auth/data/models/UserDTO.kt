package bob.colbaskin.cookly.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val email: String,
    val role: String,
    @SerialName("avatar_url")  val avatarUrl: String
)
