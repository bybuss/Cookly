package bob.colbaskin.cookly.auth.domain.network

import bob.colbaskin.cookly.auth.data.models.CodeToTokenBody
import bob.colbaskin.cookly.common.ApiResult

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun codeToToken(request: CodeToTokenBody): ApiResult<Unit>
    suspend fun login(): ApiResult<Unit>
}
