package bob.colbaskin.cookly.profile.presentation.recipe_statuses

import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipeListState

data class RecipeStatusesState(
    val selectedTab: RecipeStatusesTab = RecipeStatusesTab.Saved,
    val loadedTabs: Set<RecipeStatusesTab> = emptySet(),
    val tabStates: Map<RecipeStatusesTab, RecipeListState> =
        RecipeStatusesTab.entries.associateWith { RecipeListState() }
) {
    fun stateFor(tab: RecipeStatusesTab): RecipeListState {
        return tabStates[tab] ?: RecipeListState()
    }
}
