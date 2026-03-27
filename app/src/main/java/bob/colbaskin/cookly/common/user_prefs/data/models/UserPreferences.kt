package bob.colbaskin.cookly.common.user_prefs.data.models

data class UserPreferences(
    val agreementStatus: AgreementConfig,
    val authStatus: AuthConfig,
    val onboardingStatus: OnboardingConfig,
)
