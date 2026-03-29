package bob.colbaskin.cookly.auth.presentation

import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.BuildConfig
import bob.colbaskin.cookly.auth.data.models.CodeToTokenDTO
import bob.colbaskin.cookly.auth.domain.local.AuthDataStoreRepository
import bob.colbaskin.cookly.auth.domain.network.AuthRepository
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.inject.Inject
import androidx.core.net.toUri

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authDataStoreRepository: AuthDataStoreRepository
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    private var codeVerifier: String? = null
    private var codeChallenge: String? = null

    private val redirectUrl = "hack_app://return_app/?auth_code={auth_code}"
    private val codeChallengeMethod = "S256"
    private val clientId = "10"
    private val roleId = "2"

    init {
        onAction(AuthAction.Initialize)
    }

    fun onAction(action: AuthAction) {
        when (action) {
            AuthAction.Initialize -> initialize()
            is AuthAction.OnPageFinished -> updatePageState(action.webView)
            is AuthAction.OnAuthCodeReceived -> codeToToken(action.code)
            else -> Unit
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)

            try {
                codeVerifier = getOrGenerateCodeVerifier()
                codeChallenge = codeVerifierToPkce(codeVerifier!!)

                val authUrl = buildAuthUrl()
                val isAuthorized = authRepository.isLoggedIn()

                state = state.copy(
                    authUrl = authUrl,
                    isCheckingAuth = false,
                    isAuthorized = isAuthorized
                )

                Log.d(TAG, "Initialized auth screen. isAuthorized=$isAuthorized")
                Log.d(TAG, "Auth url: $authUrl")
            } catch (e: Exception) {
                Log.e(TAG, "Initialize error: ${e.message}", e)

                state = state.copy(
                    isCheckingAuth = false,
                    isAuthorized = false,
                    codeToTokenState = UiState.Error(
                        title = "Ошибка инициализации",
                        text = e.message ?: "Не удалось подготовить авторизацию"
                    )
                )
            }
        }
    }

    private suspend fun getOrGenerateCodeVerifier(): String {
        val savedCodeVerifier = authDataStoreRepository.getCodeVerifier().first()

        return if (!savedCodeVerifier.isNullOrEmpty()) {
            Log.d(TAG, "Use saved codeVerifier")
            savedCodeVerifier
        } else {
            val generatedCodeVerifier = generateCodeVerifier()
            authDataStoreRepository.saveCodeVerifier(generatedCodeVerifier)
            Log.d(TAG, "Generated new codeVerifier")
            generatedCodeVerifier
        }
    }

    private fun generateCodeVerifier(): String {
        val allowedCharacters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
        val randomBytes = ByteArray(32)

        SecureRandom().nextBytes(randomBytes)

        val verifier = buildString {
            randomBytes.forEach { byte ->
                val charIndex = (byte.toInt() and 0xFF) % allowedCharacters.length
                append(allowedCharacters[charIndex])
            }
        }

        return if (verifier.length < 43) {
            verifier.padEnd(43, '_')
        } else {
            verifier
        }
    }

    private fun codeVerifierToPkce(codeVerifier: String): String {
        val sha256Hash = MessageDigest
            .getInstance("SHA-256")
            .digest(codeVerifier.toByteArray())

        val challenge = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(sha256Hash)

        Log.d(TAG, "Generated codeChallenge from codeVerifier")
        return challenge
    }

    private fun buildAuthUrl(): String {
        return "${BuildConfig.BASE_API_URL}/pages/login.html".toUri()
            .buildUpon()
            .appendQueryParameter("code_verifier", codeVerifier)
            .appendQueryParameter("code_challenge_method", codeChallengeMethod)
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_url", redirectUrl)
            .appendQueryParameter("role_id", roleId)
            .build()
            .toString()
    }

    private fun codeToToken(code: String) {
        if (state.isExchangingCode) return

        Log.d(TAG, "Try codeToToken with code: $code")

        state = state.copy(
            isExchangingCode = true
        )

        viewModelScope.launch {
            val response = authRepository.codeToToken(
                request = CodeToTokenDTO(
                    authCode = code,
                    codeChallenger = codeChallenge ?: "",
                    redirectUrl = redirectUrl,
                    scopes = listOf("string")
                )
            ).toUiState()

            state = state.copy(
                codeToTokenState = response,
                isExchangingCode = false,
                isAuthorized = response is UiState.Success<*>
            )
        }
    }


    private fun updatePageState(webView: WebView) {
        state = state.copy(
            canGoBack = webView.canGoBack(),
            canGoForward = webView.canGoForward()
        )
    }
}
