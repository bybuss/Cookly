package bob.colbaskin.cookly.create_recipe.domain.models

import android.net.Uri

data class LocalImage(
    val uri: Uri,
    val displayName: String,
    val mimeType: String?,
    val sizeBytes: Long?
)
