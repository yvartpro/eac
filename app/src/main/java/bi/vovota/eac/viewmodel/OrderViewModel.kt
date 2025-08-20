package bi.vovota.eac.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.eac.data.model.NewOrder
import bi.vovota.eac.data.model.Order
import bi.vovota.eac.data.repository.OrderRepository
import bi.vovota.eac.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
  private val orderRepo: OrderRepository,
  private val userRepository: UserRepository
) : ViewModel() {
  var orders by mutableStateOf<List<Order>>(emptyList())
    private set

  var isLoading by mutableStateOf(false)
    private set

  fun loadOrders() {
    viewModelScope.launch {
      isLoading = true
      val result = orderRepo.getOrders()
      orders = result
      isLoading  = false
    }
  }

}
