package bob.colbaskin.cookly.create_recipe.domain.models

data class CreateRecipeCommand(
    val title: String,
    val description: String,
    val estimatedTime: Int,
    val caloriesBy100Grams: Int,
    val mealTime: String,
    val categories: List<CreateRecipeCategoryCommand>,
    val ingredients: List<CreateRecipeIngredientCommand>,
    val spicyLevel: Int,
    val difficultyLevel: Int,
    val steps: List<CreateRecipeStepCommand>,
    val mainPhoto: UploadImage?
)
