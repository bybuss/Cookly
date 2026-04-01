package bob.colbaskin.cookly.auth.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.navigation.Screens
import bob.colbaskin.cookly.navigation.graphs.Graphs
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowBack
import compose.icons.tablericons.ArrowForward
import compose.icons.tablericons.Refresh

private const val TAG = "AuthWebView"

@Composable
fun AuthScreenRoot(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(state.isAuthorized) {
        if (state.isAuthorized) {
            navController.navigate(Graphs.Onboarding) {
                popUpTo (Screens.WebViewAuth) { inclusive = true }
            }
        }
    }

    AuthScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is AuthAction.OnWebViewCreated -> {
                    webViewInstance = action.webView
                }
                AuthAction.OnBackClick -> {
                    webViewInstance?.goBack()
                }
                AuthAction.OnForwardClick -> {
                    webViewInstance?.goForward()
                }
                AuthAction.OnRefreshClick -> {
                    webViewInstance?.reload()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit
) {
    when {
        state.shouldShowWebView -> {
            Scaffold(
                topBar = {
                    BrowserTopBar(
                        canGoBack = state.canGoBack,
                        canGoForward = state.canGoForward,
                        onAction = onAction,
                    )
                },
                contentColor = CustomTheme.colors.text,
                containerColor = CustomTheme.colors.background
            ) { innerPadding ->
                AuthWebView(
                    startUrl = state.authUrl!!.toUri(),
                    onWebViewCreated = { onAction(AuthAction.OnWebViewCreated(it)) },
                    onPageFinished = { onAction(AuthAction.OnPageFinished(it)) },
                    onAuthCodeReceived = { onAction(AuthAction.OnAuthCodeReceived(it)) },
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }

        state.codeToTokenState is UiState.Error -> {
            val errorState = state.codeToTokenState

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorState.text.ifBlank {
                        errorState.title
                    },
                    style = CustomTheme.typography.inter.headlineLarge,
                    color = CustomTheme.colors.text
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = CustomTheme.colors.accentColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrowserTopBar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    onAction: (AuthAction) -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(
                        onClick = { onAction(AuthAction.OnBackClick) },
                        enabled = canGoBack
                    ) {
                        Icon(
                            imageVector = TablerIcons.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }

                    IconButton(
                        onClick = { onAction(AuthAction.OnForwardClick) },
                        enabled = canGoForward
                    ) {
                        Icon(
                            imageVector = TablerIcons.ArrowForward,
                            contentDescription = "Вперёд"
                        )
                    }
                }

                IconButton(
                    onClick = { onAction(AuthAction.OnRefreshClick) }
                ) {
                    Icon(
                        imageVector = TablerIcons.Refresh,
                        contentDescription = "Обновить"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CustomTheme.colors.background,
            scrolledContainerColor = CustomTheme.colors.background,
            navigationIconContentColor = CustomTheme.colors.text,
            titleContentColor = CustomTheme.colors.text,
            actionIconContentColor = CustomTheme.colors.text,
            subtitleContentColor = CustomTheme.colors.text,
        )
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AuthWebView(
    startUrl: Uri,
    onWebViewCreated: (WebView) -> Unit,
    onPageFinished: (WebView) -> Unit,
    onAuthCodeReceived: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT

            val cm = CookieManager.getInstance()
            cm.setAcceptCookie(true)
            cm.setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val currentUrl = request?.url?.toString().orEmpty()
                    Log.d(TAG, "Navigate to: $currentUrl")
                    if (currentUrl.startsWith("cookly://return_app/")) {
                        val uri = currentUrl.toUri()
                        val authCode = uri.getQueryParameter("auth_code")
                        Log.d(TAG, "Detected redirect. auth_code=$authCode")

                        authCode?.let { onAuthCodeReceived(it.trimEnd('/')) }
                        return true
                    }
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.let(onPageFinished)
                }

                override fun onReceivedError(
                    view: WebView,
                    request: WebResourceRequest,
                    error: WebResourceError
                ) {
                    Log.e(
                        TAG,
                        "onReceivedError mainFrame=${request.isForMainFrame} url=${request.url} " +
                                "code=${error.errorCode} desc=${error.description}"
                    )
                    super.onReceivedError(view, request, error)
                }

                override fun onReceivedHttpError(
                    view: WebView,
                    request: WebResourceRequest,
                    errorResponse: WebResourceResponse
                ) {
                    Log.e(
                        TAG,
                        "onReceivedHttpError mainFrame=${request.isForMainFrame} url=${request.url} " +
                                "status=${errorResponse.statusCode}"
                    )
                    super.onReceivedHttpError(view, request, errorResponse)
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(
                        TAG,
                        "${consoleMessage.messageLevel()} ${consoleMessage.sourceId()}:${consoleMessage.lineNumber()} ${consoleMessage.message()}"
                    )
                    return true
                }
            }

            onWebViewCreated(this)
        }
    }

    LaunchedEffect(startUrl) {
        webView.loadUrl(startUrl.toString())
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    AndroidView(
        factory = { webView },
        modifier = modifier
    )
}

@Preview
@Composable
private fun BrowserTopBarPreview() {
    BrowserTopBar(
        canGoBack = true,
        canGoForward = true,
        onAction = {}
    )
}
