package bob.colbaskin.cookly.onboarding_preferences.domain.models

enum class AllergyOption(val displayName: String) {
    Nuts("Орехи"),
    Eggs("Яйцо"),
    Gluten("Злаки, содержащие глютен"),
    Fish("Рыба"),
    Milk("Белок коровьего молока"),
    Peanut("Арахис"),
    FoodAdditives("Пищевые добавки"),
    Mustard("Горчица"),
    Sesame("Кунжут"),
    Shellfish("Моллюски"),
    Crustaceans("Ракообразные"),
    Strawberry("Клубника"),
    Soy("Соя"),
    Celery("Сельдерей"),
    Lupin("Люпин и продукты его переработки")
}
