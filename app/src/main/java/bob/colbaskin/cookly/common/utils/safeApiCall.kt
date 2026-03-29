package bob.colbaskin.cookly.common.utils

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.common.ApiResult
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import java.io.IOException
import bob.colbaskin.cookly.R

const val TAG = "Error"

suspend inline fun <reified  T, reified  R> safeApiCall(
    apiCall: suspend () -> T,
    successHandler: (T) -> R,
    context: Context
): ApiResult<R> {
    return try {
        val response = apiCall()
        val result = successHandler(response)
        ApiResult.Success(data = result)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
        when (e) {
            is IOException -> ApiResult.Error(
                title = context.getString(R.string.network_error_title),
                text = e.message.toString()
            )
            is TimeoutCancellationException -> ApiResult.Error(
                title = context.getString(R.string.timeout_error_title),
                text = e.message.toString()
            )
            is HttpException -> {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = errorBody ?: e.message().toString()

                when (e.code()) {
                    400 -> ApiResult.Error(
                        title = context.getString(R.string.user_credentials_error_title),
                        text = context.getString(R.string.user_credentials_error_text)
                    )
                    401 -> ApiResult.Error(
                        title = context.getString(R.string.authorization_error_title),
                        text = context.getString(R.string.authorization_error_text)
                    )
                    403 -> ApiResult.Error(
                        title = context.getString(R.string.forbidden_error_title),
                        text = context.getString(R.string.forbidden_error_text)
                    )
                    422 -> ApiResult.Error(
                        title = context.getString(R.string.fields_error_title),
                        text = context.getString(R.string.fields_error_text)
                    )
                    in 500..599 -> ApiResult.Error(
                        title = context.getString(R.string.server_error_title),
                        text = errorMessage
                    )
                    else -> ApiResult.Error(
                        title = context.getString(R.string.error_title),
                        text = errorMessage
                    )
                }
            }
            else -> ApiResult.Error(
                title = context.getString(R.string.generic_error_title),
                text = e.message.toString()
            )
        }
    }
}
