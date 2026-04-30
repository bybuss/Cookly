package bob.colbaskin.cookly.onboarding_preferences.data

import bob.colbaskin.cookly.onboarding_preferences.data.models.ExcludeIngredientGroupsBody
import bob.colbaskin.cookly.onboarding_preferences.data.models.IngredientGroupDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface OnboardingPreferencesApiService {

    @GET("/ingredient-group/list")
    suspend fun getIngredientGroups(): List<IngredientGroupDto>

    @PUT("/user/exclude-ingredients-groups")
    suspend fun setExcludeIngredientGroups(
        @Body body: ExcludeIngredientGroupsBody
    )
}
