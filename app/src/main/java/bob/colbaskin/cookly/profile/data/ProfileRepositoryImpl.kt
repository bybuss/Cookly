package bob.colbaskin.cookly.profile.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.auth.data.AuthApiService
import bob.colbaskin.cookly.auth.data.models.RefreshTokenBody
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import bob.colbaskin.cookly.common.recipe_preview.data.models.toDomain
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.di.token.TokenDataStore
import bob.colbaskin.cookly.profile.data.models.ProfileUserDto
import bob.colbaskin.cookly.profile.data.models.toDomain
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

private const val TAG = "ProfileRepository"

class ProfileRepositoryImpl @Inject constructor(
    private val context: Context,
    private val profileApiService: ProfileApiService,
    private val authApiAuthService: AuthApiService,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tokenDataStore: TokenDataStore
) : ProfileRepository {

    override fun observeUserPreferences(): Flow<User> = userPreferencesRepository.getUser()

    override suspend fun refreshUser(): ApiResult<Unit> {
        Log.d(TAG, "Refresh profile user info")
        return safeApiCall<ProfileUserDto, Unit>(
            apiCall = { profileApiService.getMe() },
            successHandler = { response ->
                userPreferencesRepository.saveUserInfo(response.toDomain())
            },
            context = context
        )
    }

    override suspend fun logout(): ApiResult<Unit> {
        Log.d(TAG, "Logout user")
        return safeApiCall<ResponseBody, Unit>(
            apiCall = { authApiAuthService.revokeToken(
                RefreshTokenBody(
                    refreshToken = tokenDataStore.getRefreshToken() ?: ""
                )
            ) },
            successHandler = {
                tokenDataStore.clearTokens()
                userPreferencesRepository.clearUserSession()
            },
            context = context
        )
    }

    override suspend fun getRecipeHistory(): ApiResult<List<RecipePreview>> {
        return safeApiCall<RecipesResponseDto, List<RecipePreview>>(
            apiCall = { profileApiService.getRecipeHistory() },
            successHandler = { response -> response.recipes.map { it.toDomain() } },
            context = context
        )
    }
}
