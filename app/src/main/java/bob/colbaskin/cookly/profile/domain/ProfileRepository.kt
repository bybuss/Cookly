package bob.colbaskin.cookly.profile.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun observeUserPreferences(): Flow<User>
    suspend fun refreshUser(): ApiResult<Unit>
    suspend fun logout(): ApiResult<Unit>
}
