package bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs

import bob.colbaskin.cookly.common.user_prefs.domain.models.UserPreferences
import bob.colbaskin.cookly.datastore.AgreementStatusOuterClass.AgreementStatus
import bob.colbaskin.cookly.datastore.AuthStatus
import bob.colbaskin.cookly.datastore.OnboardingStatus
import bob.colbaskin.cookly.datastore.RoleStatus
import bob.colbaskin.cookly.datastore.UserPreferencesProto

fun UserPreferencesProto.toDomain(): UserPreferences {
    return UserPreferences(
        agreementStatus = this.agreementStatus.toDomain(),
        authStatus = this.authStatus.toDomain(),
        onboardingStatus = this.onboardingStatus.toDomain(),
    )
}

fun AgreementStatus.toDomain(): AgreementConfig {
    return when (this) {
        AgreementStatus.NOT_ACCEPTED -> AgreementConfig.NOT_ACCEPTED
        AgreementStatus.ACCEPTED -> AgreementConfig.ACCEPTED
        AgreementStatus.UNRECOGNIZED -> AgreementConfig.NOT_ACCEPTED
    }
}

fun AuthStatus.toDomain(): AuthConfig {
    return when (this) {
        AuthStatus.AUTHENTICATED -> AuthConfig.AUTHENTICATED
        AuthStatus.NOT_AUTHENTICATED -> AuthConfig.NOT_AUTHENTICATED
        AuthStatus.UNRECOGNIZED -> AuthConfig.NOT_AUTHENTICATED
    }
}

fun OnboardingStatus.toDomain(): OnboardingConfig {
    return when (this) {
        OnboardingStatus.NOT_STARTED -> OnboardingConfig.NOT_STARTED
        OnboardingStatus.IN_PROGRESS -> OnboardingConfig.IN_PROGRESS
        OnboardingStatus.COMPLETED -> OnboardingConfig.COMPLETED
        OnboardingStatus.UNRECOGNIZED -> OnboardingConfig.NOT_STARTED
    }
}

fun RoleStatus.toDomain(): RoleConfig {
    return when (this) {
        RoleStatus.ADMIN -> RoleConfig.ADMIN
        RoleStatus.MODERATOR -> RoleConfig.MODERATOR
        RoleStatus.USER -> RoleConfig.USER
        RoleStatus.UNRECOGNIZED -> RoleConfig.USER
    }
}

fun AgreementConfig.toProto(): AgreementStatus {
    return when (this) {
        AgreementConfig.NOT_ACCEPTED -> AgreementStatus.NOT_ACCEPTED
        AgreementConfig.ACCEPTED -> AgreementStatus.ACCEPTED
    }
}

fun AuthConfig.toProto(): AuthStatus {
    return when (this) {
        AuthConfig.AUTHENTICATED -> AuthStatus.AUTHENTICATED
        AuthConfig.NOT_AUTHENTICATED -> AuthStatus.NOT_AUTHENTICATED
    }
}

fun OnboardingConfig.toProto(): OnboardingStatus {
    return when (this) {
        OnboardingConfig.NOT_STARTED -> OnboardingStatus.NOT_STARTED
        OnboardingConfig.IN_PROGRESS -> OnboardingStatus.IN_PROGRESS
        OnboardingConfig.COMPLETED -> OnboardingStatus.COMPLETED
    }
}

fun RoleConfig.toProto(): RoleStatus {
    return when (this) {
        RoleConfig.ADMIN -> RoleStatus.ADMIN
        RoleConfig.MODERATOR -> RoleStatus.MODERATOR
        RoleConfig.USER -> RoleStatus.USER
    }
}

fun String.toRoleConfig(): RoleConfig {
    return when (this.lowercase()) {
        "admin" -> RoleConfig.ADMIN
        "moderator" -> RoleConfig.MODERATOR
        "user" -> RoleConfig.USER
        else -> RoleConfig.USER
    }
}
