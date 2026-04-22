package bob.colbaskin.cookly.di.token

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val TAG = "Token"

class TokenInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = tokenDataStore.getAccessToken()

        Log.d(TAG, "Request to: ${request.url}")
        Log.d(TAG, "Headers: ${request.headers}")

        return if (!accessToken.isNullOrEmpty()) {
            Log.i(TAG, "Adding access token to headers")
            val authRequest = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(authRequest)
        } else {
            Log.e(TAG, "No access token available")
            chain.proceed(request)
        }
    }
}
