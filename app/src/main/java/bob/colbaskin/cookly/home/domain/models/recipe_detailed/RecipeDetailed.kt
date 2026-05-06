package bob.colbaskin.cookly.home.domain.models.recipe_detailed

import java.time.Instant

data class RecipeDetailed(
    val id: Int,
    val title: String,
    val description: String,
    val estimatedTime: Int,
    val caloriesBy100Grams: Int,
    val mealTime: String,
    val rating: Double,
    val ratingCount: Int,
    val spicyLevel: Int,
    val difficultyLevel: Int,
    val imageUrl: String?,
    val ingredients: List<Ingredient>,
    val steps: List<RecipeStep>,
    val categories: List<RecipeCategory>,

    val isFavorite: Boolean,
    val isAuthor: Boolean,
    val userRate: Int? = null,

    val cookingSessionId: Int? = null,

    val pubRecipeRequestId: Int? = null,
    val feedback: String? = null,
    val status: PubRecipeRequestStatus? = null,
    val reviewedAt: Instant? = null,
    val createdAt: Instant? = null,
    val existedCookingSession: Boolean = false,
) {
    val isFlameIconRed: Boolean
        get() = caloriesBy100Grams >= 300
}
