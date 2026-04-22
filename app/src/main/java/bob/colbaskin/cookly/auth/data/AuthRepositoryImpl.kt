package bob.colbaskin.cookly.auth.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import bob.colbaskin.cookly.auth.domain.network.AuthApiService
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.di.token.TokenDataStore
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val tokenDataStore: TokenDataStore,
    private val authApiService: AuthApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        val token = tokenDataStore.getAccessToken()
        return !token.isNullOrEmpty()
    }

    override suspend fun codeToToken(request: CodeToTokenDTO): ApiResult<Unit> {
        Log.d(TAG, "Attempt save tokens after codeToToken")
        return safeApiCall<Unit, Unit>(
            apiCall = {
                authApiService.codeToToken(request)
            },
            successHandler = { response ->
                Log.d(TAG, "Saving tokens after codeToToken successful")
                userPreferencesRepository.saveAuthStatus(AuthConfig.AUTHENTICATED)
            },
            context = context
        )
    }
}
