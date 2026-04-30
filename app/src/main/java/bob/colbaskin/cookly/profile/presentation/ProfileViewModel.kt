package bob.colbaskin.cookly.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    init {
        observeLocalUser()
        refreshUser()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.Refresh -> refreshUser()
            ProfileAction.Logout -> logout()
            ProfileAction.DismissError -> { state = state.copy(logoutState = UiState.Idle) }
            else -> Unit
        }
    }

    private fun observeLocalUser() {
        viewModelScope.launch {
            repository.observeUserPreferences().collectLatest { prefs ->
                state = state.copy(
                    email = prefs.email,
                    role = prefs.role,
                    avatarUrl = prefs.avatarUrl
                )
            }
        }
    }

    private fun refreshUser() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            repository.refreshUser()
            state = state.copy(isRefreshing = false)
        }
    }

    private fun logout() {
        if (state.logoutState is UiState.Loading) return

        viewModelScope.launch {
            state = state.copy(logoutState = UiState.Loading)
            val result = repository.logout()
            state = state.copy(logoutState = result.toUiState())
        }
    }
}
