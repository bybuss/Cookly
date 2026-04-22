package bob.colbaskin.cookly.auth.data.models

import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.toRoleConfig

fun UserDTO.toDomain(): User = User(
    id = this.id,
    email = this.email,
    role = this.role.toRoleConfig(),
    avatarUrl = this.avatarUrl
)
