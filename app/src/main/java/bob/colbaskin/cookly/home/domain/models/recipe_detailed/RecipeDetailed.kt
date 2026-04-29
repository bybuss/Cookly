package bob.colbaskin.cookly.home.domain.models.recipe_detailed

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
    val categories: List<RecipeCategory>
)
