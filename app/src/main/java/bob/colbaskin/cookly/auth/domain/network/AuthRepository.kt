package bob.colbaskin.cookly.auth.domain.network

import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import bob.colbaskin.cookly.common.ApiResult

interface AuthRepository {

    suspend fun isLoggedIn(): Boolean

    suspend fun codeToToken(request: CodeToTokenDTO): ApiResult<Unit>
}
