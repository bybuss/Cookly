package bob.colbaskin.cookly.auth.domain.local

import kotlinx.coroutines.flow.Flow

interface AuthDataStoreRepository {

    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    suspend fun clearToken()

    suspend fun saveRefreshToken(refreshToken: String)
    fun getRefreshToken(): Flow<String?>

    suspend fun saveCodeVerifier(codeVerifier: String)
    fun getCodeVerifier(): Flow<String?>
    suspend fun clearCodeVerifier()
}
