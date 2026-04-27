package bob.colbaskin.cookly.common.user_prefs.domain

import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    fun getAuthStatus(): Flow<AuthConfig>
    fun getUser(): Flow<User>
    suspend fun saveAgreementStatus(status: AgreementConfig)
    suspend fun saveAuthStatus(status: AuthConfig)
    suspend fun saveOnboardingStatus(status: OnboardingConfig)
    suspend fun saveUserInfo(user: User)
    suspend fun clearUserSessionPreservingAgreement()
}
