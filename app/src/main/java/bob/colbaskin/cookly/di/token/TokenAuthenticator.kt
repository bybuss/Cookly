package bob.colbaskin.cookly.di.token

import android.util.Log
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.ApiResult
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

private const val TAG = "Auth"

class TokenAuthenticator @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            Log.i(TAG, "Unauthorized. Attempting to refresh token")

            val refreshToken = tokenDataStore.getRefreshToken()

            if (!refreshToken.isNullOrEmpty()) {
                // FIXME: удалить моковый ApiResult.Success и заменить потом на рефреш через authRepository када будет
                val refreshResult = runBlocking {
                    //  authRepository.refresh(refreshToken)
                    ApiResult.Success(Unit)
                }

                if (refreshResult is ApiResult.Success<*>) {
                    val newAccessToken = tokenDataStore.getAccessToken()

                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    Log.e(TAG, "Token refresh failed")
                }
            }
        }

        return null
    }
}
