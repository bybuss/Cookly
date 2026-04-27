package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadImageResponseDto(
    @SerialName("image_path") val imagePath: String
)
