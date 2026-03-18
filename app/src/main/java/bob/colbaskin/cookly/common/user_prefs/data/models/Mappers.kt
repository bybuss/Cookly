package bob.colbaskin.cookly.common.user_prefs.data.models

import bob.colbaskin.cookly.datastore.AgreementStatusOuterClass
import bob.colbaskin.cookly.datastore.AuthStatus
import bob.colbaskin.cookly.datastore.OnboardingStatus
import bob.colbaskin.cookly.datastore.UserPreferencesProto

fun UserPreferencesProto.toData(): UserPreferences {
    return UserPreferences(
        agreementStatus = when (this.agreementStatus) {
            AgreementStatusOuterClass.AgreementStatus.NOT_ACCEPTED -> AgreementConfig.NOT_ACCEPTED
            AgreementStatusOuterClass.AgreementStatus.ACCEPTED -> AgreementConfig.ACCEPTED
            AgreementStatusOuterClass.AgreementStatus.UNRECOGNIZED, null -> AgreementConfig.NOT_ACCEPTED
        },
        authStatus = when (this.authStatus) {
            AuthStatus.AUTHENTICATED -> AuthConfig.AUTHENTICATED
            AuthStatus.NOT_AUTHENTICATED -> AuthConfig.NOT_AUTHENTICATED
            AuthStatus.UNRECOGNIZED, null -> AuthConfig.NOT_AUTHENTICATED
        },
        onboardingStatus = when (this.onboardingStatus) {
            OnboardingStatus.NOT_STARTED -> OnboardingConfig.NOT_STARTED
            OnboardingStatus.IN_PROGRESS -> OnboardingConfig.IN_PROGRESS
            OnboardingStatus.COMPLETED -> OnboardingConfig.COMPLETED
            OnboardingStatus.UNRECOGNIZED, null -> OnboardingConfig.NOT_STARTED
        }
    )
}
