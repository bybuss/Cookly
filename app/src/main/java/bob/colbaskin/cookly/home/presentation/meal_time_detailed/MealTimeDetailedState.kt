package bob.colbaskin.cookly.home.presentation.meal_time_detailed

import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.utils.getFirstLetter
import bob.colbaskin.cookly.home.domain.models.old.Meal
import bob.colbaskin.cookly.home.domain.models.old.MealTimeType

data class MealDetailedState(
    val email: String = "",
    val avatarUrl: String = "",

    val mealsList: List<Meal> = mealsListMock,
    val mealType: MealTimeType = MealTimeType.BREAKFAST,
    val currentPage: Int = 0,
    val isSheetExpanded: Boolean = false,
    val isAutoScrollEnabled: Boolean = true
) {
    val avatarLetter: String
        get() = email.getFirstLetter()
}

private val mealsListMock: List<Meal> = listOf(
    Meal(
        id = 1,
        imageId = R.drawable.smoothie_background,
        title = "Смузи с бананом и клубникой",
        description = "Смузи с бананом и клубникой - это освежающий и питательный напиток, который идеально подходит для завтрака или перекуса. Он сочетает в себе сладость банана и свежесть клубники, создавая вкусное и полезное сочетание. Этот смузи богат витаминами, минералами и антиоксидантами, что делает его отличным выбором для поддержания здоровья и энергии в течение дня."
    ),
    Meal(
        id = 2,
        imageId = R.drawable.fried_egg_backgroiund,
        title = "Мятые Иички",
        description = "Мятые иички - это яяички мятые иииичкиии"
    ),
    Meal(
        id = 3,
        imageId = R.drawable.smoothie_background,
        title = "Смузи с бананом и клубникой 2",
        description = "Смузи с бананом и клубникой - это освежающий и питательный напиток, который идеально подходит для завтрака или перекуса. Он сочетает в себе сладость банана и свежесть клубники, создавая вкусное и полезное сочетание. Этот смузи богат витаминами, минералами и антиоксидантами, что делает его отличным выбором для поддержания здоровья и энергии в течение дня."
    ),
    Meal(
        id = 4,
        imageId = R.drawable.fried_egg_backgroiund,
        title = "Мятые Иички 2",
        description = "Ни густо ни пусто"
    ),
)
