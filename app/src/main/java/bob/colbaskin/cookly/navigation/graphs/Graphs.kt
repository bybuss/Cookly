package bob.colbaskin.cookly.navigation.graphs

import kotlinx.serialization.Serializable

sealed interface Graphs {

    @Serializable
    data object Main: Graphs

    @Serializable
    data object Onboarding: Graphs

    @Serializable
    data object Detailed: Graphs

    @Serializable
    data object Agreement: Graphs

    @Serializable
    data object Auth: Graphs
}
