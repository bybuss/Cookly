package bob.colbaskin.cookly.di.token

import android.util.Log
import dagger.Lazy
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TokenExpiredInterceptor"

@Singleton
class TokenExpiredInterceptor @Inject constructor(
    private val tokenAuthenticator: Lazy<TokenAuthenticator>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isTokenExpiredResponse()) { return response }

        Log.i(TAG, "Token expired response detected. Trying to refresh token")

        val newAccessToken = tokenAuthenticator.get().refreshAccessToken()

        if (newAccessToken.isNullOrBlank()) {
            Log.e(TAG, "Cannot refresh token")
            return response
        }

        response.close()

        val retriedRequest = request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()

        return chain.proceed(retriedRequest)
    }

    private fun Response.isTokenExpiredResponse(): Boolean {
        if (code != 500 && code != 401) return false

        val bodyText = try {
            peekBody(1024 * 1024).string()
        } catch (e: Exception) {
            Log.e(TAG, "Cannot refresh token")
            return false
        }

        return bodyText.contains("Token expired.", ignoreCase = true)
    }
}
