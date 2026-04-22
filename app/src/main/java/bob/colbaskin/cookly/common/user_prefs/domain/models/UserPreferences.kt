package bob.colbaskin.cookly.common.user_prefs.domain.models

import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.OnboardingConfig

data class UserPreferences(
    val agreementStatus: AgreementConfig,
    val authStatus: AuthConfig,
    val onboardingStatus: OnboardingConfig,
)
