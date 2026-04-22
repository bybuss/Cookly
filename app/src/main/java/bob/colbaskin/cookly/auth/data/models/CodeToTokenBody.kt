package bob.colbaskin.cookly.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeToTokenBody(
    @SerialName("auth_code") val authCode: String,
    @SerialName("code_challenger") val codeChallenger: String,
)
