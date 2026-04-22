package bob.colbaskin.cookly.auth.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.auth.data.models.CodeToTokenBody
import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import bob.colbaskin.cookly.auth.data.models.UserDTO
import bob.colbaskin.cookly.auth.data.models.toDomain
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.di.token.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val tokenDataStore: TokenDataStore,
    private val authApiAuthService: AuthApiService,
    private val authApiRecipeService: AuthApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        val accessToken = tokenDataStore.getAccessToken()
        val authConfig = userPreferencesRepository.getUserPreferences().map { it.authStatus }.first()
        return !accessToken.isNullOrEmpty() && authConfig == AuthConfig.AUTHENTICATED
    }

    override suspend fun codeToToken(
        authCode: String,
        codeChallenger: String
    ): ApiResult<Unit> {
        Log.d(TAG, "Code-To-Token exchange")
        return safeApiCall<CodeToTokenDTO, Unit>(
            apiCall = { authApiAuthService.codeToToken(request = CodeToTokenBody(
                authCode = authCode,
                codeChallenger = codeChallenger
            )) },
            successHandler = { response ->
                Log.i(TAG, "Saving tokens after successful codeToToken exchange")
                tokenDataStore.saveAccessToken(response.accessToken)
                tokenDataStore.saveRefreshToken(response.refreshToken)
                login()
            },
            context = context
        )
    }

    override suspend fun login(): ApiResult<Unit> {
        Log.d(TAG, "Login in RecipeService")
        return safeApiCall<Unit, Unit>(
            apiCall = { authApiRecipeService.login() },
            successHandler = {
                Log.i(TAG, "Successful login in RecipeService")

                val meResponse = me()
                if (meResponse is ApiResult.Success<User>) {
                    Log.i(TAG, "Saving AUTHENTICATED status after successful login in RecipeService & successful getting user info")
                    userPreferencesRepository.saveAuthStatus(AuthConfig.AUTHENTICATED)
                } else  {
                    Log.e(TAG, "Saving NOT_AUTHENTICATED status after successful login in RecipeService & error of getting user info")
                    userPreferencesRepository.saveAuthStatus(AuthConfig.NOT_AUTHENTICATED)
                }
            },
            context = context
        )
    }

    override suspend fun me(): ApiResult<User> {
        Log.d(TAG, "Getting user info")
        return safeApiCall<UserDTO, User>(
            apiCall = { authApiRecipeService.me() },
            successHandler = { response ->
                Log.i(TAG, "Successfully getting user info. Save user in DataStore")
                val user = response.toDomain()
                userPreferencesRepository.saveUserInfo(user)
                user
            },
            context = context
        )
    }
}
