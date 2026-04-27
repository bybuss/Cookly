package bob.colbaskin.cookly.profile.data

import bob.colbaskin.cookly.profile.data.models.ProfileUserDto
import retrofit2.http.GET

interface ProfileApiService {

    @GET("/user/me")
    suspend fun getMe(): ProfileUserDto
}
