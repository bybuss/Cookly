package bob.colbaskin.cookly.auth.presentation

import android.webkit.WebView

sealed interface AuthAction {
    data class OnWebViewCreated(val webView: WebView): AuthAction
    data class OnPageFinished(val webView: WebView): AuthAction
    data class OnAuthCodeReceived(val code: String): AuthAction

    data object OnBackClick: AuthAction
    data object OnForwardClick: AuthAction
    data object OnRefreshClick: AuthAction
}
