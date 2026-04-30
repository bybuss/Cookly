package bob.colbaskin.cookly.auth.data

import bob.colbaskin.cookly.auth.data.models.CodeToTokenBody
import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import bob.colbaskin.cookly.auth.data.models.RefreshTokenBody
import bob.colbaskin.cookly.auth.data.models.UserDTO
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/code-to-token")
    suspend fun codeToToken(@Body request: CodeToTokenBody): CodeToTokenDTO

    @GET("/auth/login")
    suspend fun login()

    @GET("/user/me")
    suspend fun me(): UserDTO

    @POST("/client/refresh")
    suspend fun refresh(@Body request: RefreshTokenBody): String

    @POST("/client/revoke")
    suspend fun revokeToken(@Body request: RefreshTokenBody): ResponseBody
}
