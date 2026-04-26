package bob.colbaskin.cookly.create_recipe.domain.models

sealed interface PhotoTarget {
    data object Main : PhotoTarget
    data class Step(val stepLocalId: Long) : PhotoTarget
}
