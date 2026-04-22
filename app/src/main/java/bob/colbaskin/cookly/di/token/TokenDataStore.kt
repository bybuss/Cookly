package bob.colbaskin.cookly.di.token

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN_DATA_STORE_NAME = "token_data_store"
private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_DATA_STORE_NAME)
private const val TAG = "Token"

@Singleton
class TokenDataStore @Inject constructor(context: Context) {
    private val dataStore = context.tokenDataStore
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveAccessToken(accessToken: String) {
        Log.d(TAG, "saving access_token: $accessToken")
        dataStore.edit {  preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        Log.d(TAG, "saving refresh_token: $refreshToken")
        dataStore.edit {  preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    private val accessToken: Flow<String?> = dataStore.data.map { it[ACCESS_TOKEN] }
    fun getAccessToken(): String? = runBlocking { accessToken.first() }

    private val refreshToken: Flow<String?> = dataStore.data.map { it[REFRESH_TOKEN] }
    fun getRefreshToken(): String? = runBlocking { refreshToken.first() }

    suspend fun clearTokens() {
        Log.d(TAG, "Clearing tokens")
        dataStore.edit { it.clear() }
    }
}
