package bob.colbaskin.cookly.agreement.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.user_prefs.data.models.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreementViewModel @Inject constructor(
    private val userPreferences: UserPreferencesRepository
): ViewModel() {
    var state by mutableStateOf(AgreementState())
        private set

    fun onAction(action: AgreementAction) {
        when (action) {
            is AgreementAction.UpdateAccept -> updateAccept(action.isRulesAccepted)
            is AgreementAction.IAgree -> iAgree()
            else -> Unit
        }
    }

    private fun updateAccept(isRulesAccepted: Boolean) {
        state = state.copy(isRulesAccepted = !isRulesAccepted)
    }

    private fun iAgree() {
        viewModelScope.launch {
            userPreferences.saveAgreementStatus(AgreementConfig.ACCEPTED)
        }
    }
}
