package bob.colbaskin.cookly.auth.presentation

import bob.colbaskin.cookly.common.UiState

data class AuthState(
    val authUrl: String? = null,
    val isCheckingAuth: Boolean = true,
    val isAuthorized: Boolean = false,
    val isExchangingCode: Boolean = false,
    val codeToTokenState: UiState<Unit> = UiState.Loading,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false
) {
    val isLoading: Boolean
        get() = isCheckingAuth || isExchangingCode

    val shouldShowWebView: Boolean
        get() = !isLoading && !isAuthorized && authUrl != null
}
