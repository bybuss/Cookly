package bob.colbaskin.cookly.create_recipe.domain.models

data class CreateRecipeStepCommand(
    val number: Int,
    val title: String,
    val description: String,
    val image: UploadImage?
)
