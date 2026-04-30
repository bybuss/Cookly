package bob.colbaskin.cookly.di.token

import android.util.Log
import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.auth.data.models.RefreshTokenBody
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val TAG = "Auth"

@Singleton
class TokenAuthenticator @Inject constructor(
    @param:Named("AuthApiAuthService") private val authApiService: AuthApiService,
    private val tokenDataStore: TokenDataStore
) : Authenticator {

    private val refreshLock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null

        if (response.responseCount >= 2) {
            Log.e(TAG, "Token refresh stopped: too many auth attempts")
            return null
        }

        val newAccessToken = refreshAccessToken() ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    fun refreshAccessToken(): String? {
        return synchronized(refreshLock) {
            val refreshToken = tokenDataStore.getRefreshToken()

            if (refreshToken.isNullOrBlank()) {
                Log.e(TAG, "Refresh token is empty")
                return@synchronized null
            }

            runBlocking {
                try {
                    Log.i(TAG, "Refreshing access token")

                    val newAccessToken = authApiService.refresh(
                        RefreshTokenBody(refreshToken = refreshToken)
                    )

                    tokenDataStore.saveAccessToken(newAccessToken)

                    Log.i(TAG, "Access token refreshed successfully")
                    newAccessToken
                } catch (e: Exception) {
                    Log.e(TAG, "Token refresh failed", e)
                    null
                }
            }
        }
    }

    private val Response.responseCount: Int
        get() {
            var currentResponse: Response? = this
            var count = 1

            while (currentResponse?.priorResponse != null) {
                count++
                currentResponse = currentResponse.priorResponse
            }

            return count
        }
}
