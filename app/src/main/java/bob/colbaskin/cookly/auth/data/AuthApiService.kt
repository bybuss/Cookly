package bob.colbaskin.cookly.auth.data

import bob.colbaskin.cookly.auth.data.models.CodeToTokenBody
import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/code-to-token")
    suspend fun codeToToken(@Body request: CodeToTokenBody): CodeToTokenDTO

    @GET("/auth/login")
    suspend fun login()
}