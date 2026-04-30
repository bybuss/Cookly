package bob.colbaskin.cookly.onboarding_preferences.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup

interface OnboardingPreferencesRepository {
    suspend fun getIngredientGroups(): ApiResult<List<IngredientGroup>>

    suspend fun setExcludeIngredientGroups(
        ingredientGroupIds: List<Int>
    ): ApiResult<Unit>
}
