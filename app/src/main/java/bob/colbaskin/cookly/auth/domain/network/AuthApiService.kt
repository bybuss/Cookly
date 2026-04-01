package bob.colbaskin.cookly.auth.domain.network

import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/code-to-token")
    suspend fun codeToToken(@Body request: CodeToTokenDTO)
}
