package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.PAYMENT_STATUS
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateOrderRequest
import com.nrin31266.ecommercemultivendor.domain.usecase.payment.CreateOrderUseCase
import com.vanrin05.app.domain.PAYMENT_METHOD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _checkoutState = MutableStateFlow(CheckoutState())
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()
    private val _createOrderState = MutableStateFlow(CreateOrderState())
    val createOrderState: StateFlow<CreateOrderState> = _createOrderState.asStateFlow()

    private val _checkoutEvent = MutableSharedFlow<CheckoutEvent>()
    val checkoutEvent = _checkoutEvent.asSharedFlow()

    fun createOrder(addressId: Long) {
        viewModelScope.launch {
            createOrderUseCase(
                CreateOrderRequest(
                    paymentMethod = createOrderState.value.paymentMethod,
                    addressId = addressId
                )
            ).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _createOrderState.value = _createOrderState.value.copy(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _createOrderState.value = _createOrderState.value.copy(isLoading = false)
                        if (it.data.payment_link_url != null) {
                            _checkoutEvent.emit(CheckoutEvent.OpenLinkPayment(it.data.payment_link_url))
                        }else{
                            _checkoutEvent.emit(CheckoutEvent.PaymentSuccess(""))
                        }
                    }
                    is ResultState.Error -> {
                        _createOrderState.value = _createOrderState.value.copy(isLoading = false,
                            errorMessage = it.message)
                        _checkoutEvent.emit(CheckoutEvent.PaymentFailed(it.message))
                    }
                }
            }

        }
    }

    fun selectPaymentMethod(method: PAYMENT_METHOD) {
        _createOrderState.value = _createOrderState.value.copy(paymentMethod = method)
    }

}

data class CheckoutState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

)
data class CreateOrderState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val paymentMethod: PAYMENT_METHOD = PAYMENT_METHOD.CASH_ON_DELIVERY,
    val totalShippingCosts: Long = 0,
)


sealed class CheckoutEvent {
    data class ShowSnackbar(val message: String) : CheckoutEvent()
    data class PaymentSuccess(val message: String) : CheckoutEvent()
    data class PaymentFailed(val message: String) : CheckoutEvent()
    data class OpenLinkPayment(val link: String) : CheckoutEvent()
}