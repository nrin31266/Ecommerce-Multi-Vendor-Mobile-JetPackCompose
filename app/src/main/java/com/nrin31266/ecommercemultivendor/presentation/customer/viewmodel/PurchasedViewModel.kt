package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.domain.usecase.payment.GetUserOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PurchasedViewModel @Inject constructor(
    private val getUserOrdersUseCase: GetUserOrdersUseCase
) : ViewModel() {
    private val _purchasedState = MutableStateFlow(PurchasedState())
    val purchasedState = _purchasedState.asStateFlow()
    private val _toPayState = MutableStateFlow(ToPayState())
    val toPayState = _toPayState.asStateFlow()
    private val _toConfirmState = MutableStateFlow(ToConfirmState())
    val toConfirmState = _toConfirmState.asStateFlow()
    private val _toPickupState = MutableStateFlow(ToPickupState())
    val toPickupState = _toPickupState.asStateFlow()
    private val _shippingState = MutableStateFlow(ShippingState())
    val shippingState = _shippingState.asStateFlow()
    private val _deliveredState = MutableStateFlow(DeliveredState())
    val deliveredState = _deliveredState.asStateFlow()
    private val _cancelledState = MutableStateFlow(CancelledState())
    val cancelledState = _cancelledState.asStateFlow()


    fun selectTab(tabIndex: Int) {
        _purchasedState.value = _purchasedState.value.copy(tabIndex = tabIndex)

    }

    fun toConfirmAction() {
        viewModelScope.launch {
            getUserOrdersUseCase(SELLER_ORDER_STATUS.PENDING).collectLatest {
                when (it) {

                    is ResultState.Loading -> {

                        _toConfirmState.value = _toConfirmState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _toConfirmState.value =
                            _toConfirmState.value.copy(isLoading = false, toConfirmList = it.data)
                    }

                    is ResultState.Error -> {

                        _toConfirmState.value =
                            _toConfirmState.value.copy(isLoading = false, errorMessage = it.message)
                    }
                }
            }
        }
    }

    fun toPickupAction() {
        viewModelScope.launch {
            getUserOrdersUseCase(SELLER_ORDER_STATUS.CONFIRMED).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _toPickupState.value = _toPickupState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _toPickupState.value =
                            _toPickupState.value.copy(isLoading = false, toPickupList = it.data)
                    }

                    is ResultState.Error -> {
                        _toPickupState.value =
                            _toPickupState.value.copy(isLoading = false, errorMessage = it.message)
                    }
                }

            }

        }
    }

}

data class PurchasedState(
    val isLoading: Boolean = false,
    val tabIndex: Int? = null,
)

data class ToPayState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val toPayList: List<Any> = emptyList()
)

data class ToConfirmState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val toConfirmList: List<SellerOrderDto>? = null
)

data class ToPickupState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val toPickupList: List<SellerOrderDto>? = null
)

data class ShippingState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val shippingList: List<SellerOrderDto>? = null
)

data class DeliveredState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val deliveredList: List<SellerOrderDto>? = null
)

data class CancelledState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val cancelledList: List<OrderItemDto> = emptyList()
)


