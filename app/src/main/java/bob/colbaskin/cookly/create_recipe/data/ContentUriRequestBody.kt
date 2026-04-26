package bob.colbaskin.cookly.create_recipe.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ContentUriRequestBody(
    private val context: Context,
    private val uri: Uri,
    private val mimeType: String?
) : RequestBody() {

    override fun contentType(): MediaType? = mimeType?.toMediaTypeOrNull()

    override fun contentLength(): Long {
        return context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst() && sizeIndex != -1 && !cursor.isNull(sizeIndex)) {
                cursor.getLong(sizeIndex)
            } else {
                -1L
            }
        } ?: -1L
    }

    override fun writeTo(sink: BufferedSink) {
        val inputStream = requireNotNull(context.contentResolver.openInputStream(uri)) {
            "Не удалось открыть поток для URI: $uri"
        }

        inputStream.use { input ->
            sink.writeAll(input.source())
        }
    }
}

fun createMultipartBodyPart(
    context: Context,
    uri: Uri,
    fileName: String,
    mimeType: String?
): MultipartBody.Part {
    val body = ContentUriRequestBody(
        context = context,
        uri = uri,
        mimeType = mimeType
    )

    return MultipartBody.Part.createFormData(
        name = "file",
        filename = fileName,
        body = body
    )
}
