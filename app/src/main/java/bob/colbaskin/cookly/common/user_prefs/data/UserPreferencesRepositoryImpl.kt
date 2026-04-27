package bob.colbaskin.cookly.common.user_prefs.data

import bob.colbaskin.cookly.common.user_prefs.data.data_store.UserDataStore
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.UserPreferences
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserDataStore
): UserPreferencesRepository {
    override fun getUserPreferences(): Flow<UserPreferences> = dataStore.getUserPreferences()

    override fun getAuthStatus(): Flow<AuthConfig> = dataStore.getAuthStatus()

    override fun getUser(): Flow<User> = dataStore.getUser()

    override suspend fun saveAgreementStatus(status: AgreementConfig)
        = dataStore.saveAgreementStatus(status)

    override suspend fun saveAuthStatus(status: AuthConfig) = dataStore.saveAuthStatus(status)

    override suspend fun saveOnboardingStatus(status: OnboardingConfig)
        = dataStore.saveOnboardingStatus(status)

    override suspend fun saveUserInfo(user: User)
        = dataStore.saveUserInfo(user)

    override suspend fun clearUserSessionPreservingAgreement() {
        dataStore.clearUserSessionPreservingAgreement()
    }
}
