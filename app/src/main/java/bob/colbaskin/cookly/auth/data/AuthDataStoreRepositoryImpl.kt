package bob.colbaskin.cookly.auth.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import bob.colbaskin.cookly.auth.domain.local.AuthDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "Auth"
val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token_data_store")

class AuthDataStoreRepositoryImpl @Inject constructor(
    context: Context
): AuthDataStoreRepository {

    private object PreferencesKey {
        val token = stringPreferencesKey(name = "token")
        val refreshToken = stringPreferencesKey(name = "refreshToken")
        val codeVerifier = stringPreferencesKey(name = "codeVerifier")
    }
    private val dataStore = context.tokenDataStore

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.token] = token
            Log.d(TAG, "token saved in datastore: $token")
        }
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val token = preferences[PreferencesKey.token]
                Log.d(TAG, "Get token from datastore: $token")
                token
            }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.refreshToken] = refreshToken
            Log.d(TAG, "refresh token saved: $refreshToken")
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val refreshToken = preferences[PreferencesKey.refreshToken]
                Log.d(TAG, "Get refresh token from datastore: $refreshToken")
                refreshToken
            }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKey.token)
            Log.d(TAG, "Cleared token")
        }
    }

    override suspend fun saveCodeVerifier(codeVerifier: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.codeVerifier] = codeVerifier
            Log.d(TAG, "Saved code verifier: $codeVerifier")
        }
    }

    override fun getCodeVerifier(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKey.codeVerifier]
            }
    }

    override suspend fun clearCodeVerifier() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKey.codeVerifier)
            Log.d(TAG, "Cleared code verifier")
        }
    }
}
