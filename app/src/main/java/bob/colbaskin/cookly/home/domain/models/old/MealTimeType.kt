package bob.colbaskin.cookly.home.domain.models.old

enum class MealTimeType(
    val apiValue: String,
    val title: String
) {
    BREAKFAST(
        apiValue = "breakfast",
        title = "Завтрак"
    ),
    LUNCH(
        apiValue = "lunch",
        title = "Обед"
    ),
    SUPPER(
        apiValue = "supper",
        title = "Ужин"
    )
}
