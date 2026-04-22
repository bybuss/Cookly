package bob.colbaskin.cookly.common.user_prefs.domain.models

import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.RoleConfig

data class User(
    val id: String,
    val email: String,
    val role: RoleConfig,
    val avatarUrl: String
)
