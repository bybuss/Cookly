package bob.colbaskin.cookly.onboarding_preferences.data

import android.content.Context
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.onboarding_preferences.data.models.ExcludeIngredientGroupsBody
import bob.colbaskin.cookly.onboarding_preferences.data.models.IngredientGroupDto
import bob.colbaskin.cookly.onboarding_preferences.data.models.toDomain
import bob.colbaskin.cookly.onboarding_preferences.domain.OnboardingPreferencesRepository
import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup
import javax.inject.Inject

class OnboardingPreferencesRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: OnboardingPreferencesApiService
) : OnboardingPreferencesRepository {

    override suspend fun getIngredientGroups(): ApiResult<List<IngredientGroup>> {
        return safeApiCall<List<IngredientGroupDto>, List<IngredientGroup>>(
            apiCall = { apiService.getIngredientGroups() },
            successHandler = { response ->
                response.map { it.toDomain() }
            },
            context = context
        )
    }

    override suspend fun setExcludeIngredientGroups(
        ingredientGroupIds: List<Int>
    ): ApiResult<Unit> {
        return safeApiCall<Unit, Unit>(
            apiCall = {
                apiService.setExcludeIngredientGroups(
                    ExcludeIngredientGroupsBody(
                        ingredientGroupIds = ingredientGroupIds
                    )
                )
            },
            successHandler = { Unit },
            context = context
        )
    }
}
