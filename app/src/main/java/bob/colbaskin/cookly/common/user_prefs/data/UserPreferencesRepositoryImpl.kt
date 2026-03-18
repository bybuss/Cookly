package bob.colbaskin.cookly.common.user_prefs.data

import bob.colbaskin.cookly.common.user_prefs.data.dataStore.UserDataStore
import bob.colbaskin.cookly.common.user_prefs.data.models.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserDataStore
): UserPreferencesRepository {
    override fun getUserPreferences(): Flow<UserPreferences> = dataStore.getUserPreferences()

    override suspend fun saveAgreementStatus(status: AgreementConfig)
        = dataStore.saveAgreementStatus(status)

    override suspend fun saveAuthStatus(status: AuthConfig) = dataStore.saveAuthStatus(status)

    override suspend fun saveOnboardingStatus(status: OnboardingConfig)
        = dataStore.saveOnboardingStatus(status)
}
