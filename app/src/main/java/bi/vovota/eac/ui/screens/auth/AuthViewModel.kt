package bi.vovota.eac.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.eac.data.remote.dto.AuthLogin
import bi.vovota.eac.data.remote.dto.AuthRegister
import bi.vovota.eac.data.remote.dto.AuthResponse
import bi.vovota.eac.data.repo.AuthRepo
import bi.vovota.eac.data.TokenManager
import bi.vovota.eac.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepo,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _registerUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val registerUiState: StateFlow<UiState<Unit>> = _registerUiState

    private val _loginUiState = MutableStateFlow<UiState<AuthResponse>>(UiState.Idle)
    val loginUiState: StateFlow<UiState<AuthResponse>> = _loginUiState

    fun login(request: AuthLogin) {
        viewModelScope.launch {
            _loginUiState.value = UiState.Loading
            val result = repository.login(request)
            result.fold(
                onSuccess = { resp ->
                    tokenManager.saveAccessToken(resp.access)
                    tokenManager.saveRefreshToken(resp.refresh)
                    _loginUiState.value = UiState.Success(resp)
                },
                onFailure = { e ->
                    Log.e("AuthViewModel login error", "Login failed", e)
                    _loginUiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }

    val accessToken = tokenManager.accessToken.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    fun register(name: String, password: String) {
        viewModelScope.launch {
            _registerUiState.value = UiState.Loading
            val result = repository.register(AuthRegister(name, password))
            result.fold(
                onSuccess = { _registerUiState.value = UiState.Success(Unit) },
                onFailure = { _registerUiState.value = UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun resetLoginState() {
        _loginUiState.value = UiState.Idle
    }

    fun resetRegisterState() {
        _registerUiState.value = UiState.Idle
    }
}