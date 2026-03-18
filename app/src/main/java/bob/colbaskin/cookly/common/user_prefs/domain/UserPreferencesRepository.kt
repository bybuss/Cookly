package bob.colbaskin.cookly.common.user_prefs.domain

import bob.colbaskin.cookly.common.user_prefs.data.models.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun saveAgreementStatus(status: AgreementConfig)
    suspend fun saveAuthStatus(status: AuthConfig)
    suspend fun saveOnboardingStatus(status: OnboardingConfig)
}
