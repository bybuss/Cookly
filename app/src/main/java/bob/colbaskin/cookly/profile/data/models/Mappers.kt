package bob.colbaskin.cookly.profile.data.models

import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.toRoleConfig

fun ProfileUserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        role = role.toRoleConfig(),
        avatarUrl = avatarUrl.orEmpty()
    )
}
