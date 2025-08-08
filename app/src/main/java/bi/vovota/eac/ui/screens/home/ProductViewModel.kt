package bi.vovota.eac.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.eac.data.remote.dto.ProfileResponse
import bi.vovota.eac.data.repo.ProductRepo
import bi.vovota.eac.data.repo.ProfileRepo
import bi.vovota.eac.model.Product
import bi.vovota.eac.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repo: ProductRepo): ViewModel() {
    private val _productUiState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val productUiState: MutableStateFlow<UiState<List<Product>>> = _productUiState

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun loadProducts(token: String) {
        viewModelScope.launch {
            _productUiState.value = UiState.Loading
            val result = repo.getProducts()
            result.fold(
                onSuccess = { pList ->
                    _productUiState.value = UiState.Success(pList)
                    _products.value = pList
                },
                onFailure = { e ->
                    _productUiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }
}