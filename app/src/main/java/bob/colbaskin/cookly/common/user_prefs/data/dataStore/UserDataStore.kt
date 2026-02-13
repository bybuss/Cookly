package bob.colbaskin.cookly.common.user_prefs.data.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import bob.colbaskin.cookly.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.cookly.common.user_prefs.data.models.toData
import bob.colbaskin.cookly.datastore.AuthStatus
import bob.colbaskin.cookly.datastore.OnboardingStatus
import bob.colbaskin.cookly.datastore.UserPreferencesProto
import bob.colbaskin.cookly.datastore.copy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_FILE_NAME = "user_preferences_cookly.pb"
private const val TAG = "UserPreferences"

private val Context.userPreferencesDataStore: DataStore<UserPreferencesProto> by dataStore(
    fileName = USER_PREFERENCES_FILE_NAME,
    serializer = UserPreferencesSerializer
)

class UserDataStore(context: Context) {
    private val dataStore: DataStore<UserPreferencesProto> = context.userPreferencesDataStore

    fun getUserPreferences(): Flow<UserPreferences> = dataStore.data.map { it.toData() }

    suspend fun saveAuthStatus(status: AuthConfig) {
        Log.d(TAG, "saveAuthStatus: $status")
        dataStore.updateData { prefs ->
            prefs.copy {
                authStatus = when (status) {
                    AuthConfig.AUTHENTICATED -> AuthStatus.AUTHENTICATED
                    AuthConfig.NOT_AUTHENTICATED -> AuthStatus.NOT_AUTHENTICATED
                }
            }
        }
    }

    suspend fun saveOnboardingStatus(status: OnboardingConfig) {
        Log.d(TAG, "saveOnboardingStatus: $status")
        dataStore.updateData { prefs ->
            prefs.copy {
                onboardingStatus = when (status) {
                    OnboardingConfig.NOT_STARTED -> OnboardingStatus.NOT_STARTED
                    OnboardingConfig.IN_PROGRESS -> OnboardingStatus.IN_PROGRESS
                    OnboardingConfig.COMPLETED -> OnboardingStatus.COMPLETED
                }
            }
        }
    }
}
