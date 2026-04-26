package bob.colbaskin.cookly.create_recipe.domain.models

import android.net.Uri

data class UploadImage(
    val uri: Uri,
    val fileName: String,
    val mimeType: String?
)
