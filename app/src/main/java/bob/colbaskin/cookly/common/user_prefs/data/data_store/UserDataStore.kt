package bob.colbaskin.cookly.common.user_prefs.data.data_store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.UserPreferences
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.toDomain
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.toProto
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

    fun getUserPreferences(): Flow<UserPreferences> = dataStore.data.map { it.toDomain() }

    fun getAuthStatus(): Flow<AuthConfig> = dataStore.data.map { it.authStatus.toDomain() }

    suspend fun saveAgreementStatus(status: AgreementConfig) {
        Log.d(TAG, "saveAgreementStatus: $status")
        dataStore.updateData { prefs ->
            prefs.copy { agreementStatus = status.toProto() }
        }
    }

    suspend fun saveAuthStatus(status: AuthConfig) {
        Log.d(TAG, "saveAuthStatus: $status")
        dataStore.updateData { prefs ->
            prefs.copy { authStatus = status.toProto() }
        }
    }

    suspend fun saveOnboardingStatus(status: OnboardingConfig) {
        Log.d(TAG, "saveOnboardingStatus: $status")
        dataStore.updateData { prefs ->
            prefs.copy { onboardingStatus = status.toProto() }
        }
    }

    suspend fun saveUserInfo(user: User) {
        Log.d(TAG, "Save user info: $user")
        dataStore.updateData { prefs ->
            prefs.copy {
                id = user.id
                email = user.email
                role = user.role.toProto()
                avatarUrl = user.avatarUrl
            }
        }
    }
}
