package bob.colbaskin.cookly.profile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUserDto(
    val id: String,
    val email: String,
    val role: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)
