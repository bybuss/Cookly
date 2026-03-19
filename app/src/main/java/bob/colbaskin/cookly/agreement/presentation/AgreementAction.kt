package bob.colbaskin.cookly.agreement.presentation

sealed interface AgreementAction {
    data object Back: AgreementAction
    data class UpdateAccept(val isRulesAccepted: Boolean): AgreementAction
    data object NavigateToPolicy: AgreementAction
    data object NavigateToTermsOfUse: AgreementAction
    data object IAgree: AgreementAction
}