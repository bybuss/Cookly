package bob.colbaskin.cookly.profile.presentation

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.RoleConfig

data class ProfileState(
    val email: String = "",
    val role: RoleConfig = RoleConfig.USER,
    val avatarUrl: String = "",

    val isRefreshing: Boolean = false,
    val refreshError: String? = null,

    val logoutState: UiState<Unit> = UiState.Idle
) {
    val isModeratorOrAdmin: Boolean
        get() = role == RoleConfig.MODERATOR || role == RoleConfig.ADMIN

    val displayName: String
        get() = when (role) {
            RoleConfig.ADMIN -> "Администратор"
            RoleConfig.MODERATOR -> "Модератор"
            RoleConfig.USER -> "Пользователь"
        }

    val avatarLetter: String
        get() = email.firstOrNull()
            ?.uppercaseChar()
            ?.toString()
            ?: "П"
}
