package bob.colbaskin.cookly.create_recipe.presentation

import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategory
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredient
import bob.colbaskin.cookly.create_recipe.domain.models.LocalImage

sealed interface CreateRecipeAction {
    data object Back : CreateRecipeAction

    data class UpdateTitle(val value: String): CreateRecipeAction
    data class UpdateDescription(val value: String): CreateRecipeAction
    data class UpdateCaloriesBy100Grams(val value: String): CreateRecipeAction
    data class UpdateMealTime(val value: String):  CreateRecipeAction

    data object OpenTimePicker: CreateRecipeAction
    data object DismissTimePicker: CreateRecipeAction
    data class ConfirmTime(val hour: Int, val minute: Int): CreateRecipeAction

    data object ShowCategorySheet: CreateRecipeAction
    data object HideCategorySheet: CreateRecipeAction
    data class AddCategory(val category: CreateRecipeCategory): CreateRecipeAction
    data class RemoveCategory(val categoryId: Int): CreateRecipeAction

    data object ShowIngredientSheet: CreateRecipeAction
    data object HideIngredientSheet: CreateRecipeAction
    data class AddIngredient(val ingredient: CreateRecipeIngredient): CreateRecipeAction
    data class RemoveIngredient(val ingredientId: Int): CreateRecipeAction
    data class MoveIngredient(val fromIndex: Int, val toIndex: Int): CreateRecipeAction

    data object AddStep: CreateRecipeAction
    data class UpdateStepTitle(val stepLocalId: Long, val value: String): CreateRecipeAction
    data class UpdateStepDescription(val stepLocalId: Long, val value: String): CreateRecipeAction
    data class SetStepPhoto(val stepLocalId: Long, val image: LocalImage?): CreateRecipeAction
    data class RemoveStep(val stepLocalId: Long): CreateRecipeAction
    data class MoveStep(val fromIndex: Int, val toIndex: Int): CreateRecipeAction

    data class SetMainPhoto(val image: LocalImage?): CreateRecipeAction
    data object RemoveMainPhoto: CreateRecipeAction

    data object Submit: CreateRecipeAction
    data object ConsumeSuccess: CreateRecipeAction
    data object DismissError: CreateRecipeAction

    data class ShowSheet(val sheet: CreateRecipeBottomSheet) : CreateRecipeAction
    data object HideSheet : CreateRecipeAction

    data class ToggleCategory(val id: Int) : CreateRecipeAction
    data object ConfirmCategories : CreateRecipeAction

    data class UpdateIngredientGroupQuery(val value: String) : CreateRecipeAction
    data class ToggleIngredientGroup(val id: Int) : CreateRecipeAction
    data object ConfirmIngredientGroups : CreateRecipeAction
}
