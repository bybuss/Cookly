package bob.colbaskin.cookly.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("access_token") val accessToken: String
)
