package bob.colbaskin.cookly.profile.presentation.recipe_statuses

enum class RecipeStatusesTab(
    val title: String
) {
    Saved(title = "Сохраненные"),
    Moderating(title = "На модерации"),
    Rejected(title = "Отклоненные"),
    Published(title = "Опубликованные")
}
