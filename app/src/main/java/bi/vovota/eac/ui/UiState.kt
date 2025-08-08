package bi.vovota.eac.ui

sealed class UiState<out T> {
    object Idle: UiState<Nothing>()
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val sapor: String): UiState<Nothing>()
}