package bob.colbaskin.cookly.profile.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.user_prefs.domain.models.UserPreferences
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.di.token.TokenDataStore
import bob.colbaskin.cookly.profile.data.models.ProfileUserDto
import bob.colbaskin.cookly.profile.data.models.toDomain
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "ProfileRepository"

class ProfileRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: ProfileApiService,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tokenDataStore: TokenDataStore
) : ProfileRepository {

    override fun observeUserPreferences(): Flow<User> = userPreferencesRepository.getUser()

    override suspend fun refreshUser(): ApiResult<Unit> {
        Log.d(TAG, "Refresh profile user info")
        return safeApiCall<ProfileUserDto, Unit>(
            apiCall = { apiService.getMe() },
            successHandler = { response ->
                userPreferencesRepository.saveUserInfo(response.toDomain())
            },
            context = context
        )
    }

    override suspend fun logout() {
        Log.d(TAG, "Logout user")
        tokenDataStore.clearTokens()
        userPreferencesRepository.clearUserSessionPreservingAgreement()
    }
}
