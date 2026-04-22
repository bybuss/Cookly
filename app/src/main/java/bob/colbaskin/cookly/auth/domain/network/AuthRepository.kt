package bob.colbaskin.cookly.auth.domain.network

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.user_prefs.domain.models.User

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun codeToToken(
        authCode: String,
        codeChallenger: String
    ): ApiResult<Unit>
    suspend fun login(): ApiResult<Unit>
    suspend fun me(): ApiResult<User>
}
