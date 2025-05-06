package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.domain.usecase.payment.GetUserOrdersUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.purchased.UserCancelSellerOrderUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.purchased.UserConfirmSellerOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PurchasedViewModel @Inject constructor(
    private val getUserOrdersUseCase: GetUserOrdersUseCase,
    private val userCancelSellerOrderUseCase: UserCancelSellerOrderUseCase,
    private val userConfirmSellerOrderUseCase: UserConfirmSellerOrderUseCase
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
    private val _eventFlow = MutableSharedFlow<PurchasedEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun selectTab(tabIndex: Int) {
        _purchasedState.value = _purchasedState.value.copy(tabIndex = tabIndex)

    }

    fun shippingAction() {
        viewModelScope.launch {
            _shippingState.value = _shippingState.value.copy(isLoading = true)

            combine(
                getUserOrdersUseCase(SELLER_ORDER_STATUS.SHIPPING),
                getUserOrdersUseCase(SELLER_ORDER_STATUS.DELIVERED)
            ) { shippingResult, deliveredResult ->

                when {
                    shippingResult is ResultState.Success && deliveredResult is ResultState.Success -> {
                        val combinedList = deliveredResult.data + shippingResult.data
                        _shippingState.value = _shippingState.value.copy(
                            isLoading = false,
                            shippingList = combinedList
                        )
                    }

                    shippingResult is ResultState.Error -> {
                        _shippingState.value = _shippingState.value.copy(
                            isLoading = false,
                            errorMessage = shippingResult.message
                        )
                    }

                    deliveredResult is ResultState.Error -> {
                        _shippingState.value = _shippingState.value.copy(
                            isLoading = false,
                            errorMessage = deliveredResult.message
                        )
                    }

                    else -> {
                        _shippingState.value = _shippingState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }.collect()
        }
    }

    fun userConfirmSellerOrder(sellerOrderId: Long) {
        viewModelScope.launch {
            userConfirmSellerOrderUseCase(sellerOrderId).collectLatest {
                when (it) {
                    is ResultState.Loading -> {

                    }

                    is ResultState.Success -> {
                        _eventFlow.emit(PurchasedEvent.PopToTab(4))
                        _shippingState.value = _shippingState.value.copy(shippingList =
                        _shippingState.value.shippingList?.filter { it.id != sellerOrderId })
                        val confirmedOrder = it.data
                        if (deliveredState.value.deliveredList == null) {
                            deliveredAction()
                        } else {
                            val newDeliveredList: List<SellerOrderDto> =
                                listOf(confirmedOrder) + deliveredState.value.deliveredList!!
                            _deliveredState.value =
                                _deliveredState.value.copy(deliveredList = newDeliveredList)
                        }
                    }

                    is ResultState.Error -> {

                    }

                }
            }

        }
    }


    fun userCancelSellerOrder(sellerOrderId: Long) {
        viewModelScope.launch {
            userCancelSellerOrderUseCase(sellerOrderId).collectLatest {
                when (it) {

                    is ResultState.Loading -> {

                    }

                    is ResultState.Success -> {
                        _eventFlow.emit(PurchasedEvent.PopToTab(5))
                        _toConfirmState.value = _toConfirmState.value.copy(toConfirmList =
                        _toConfirmState.value.toConfirmList?.filter { it.id != sellerOrderId })
                        val cancelledOrder = it.data

                        if (cancelledState.value.cancelledList == null) {
                            cancelAction()
                        } else {
                            val newCancelledList: List<SellerOrderDto> =
                                listOf(cancelledOrder) + cancelledState.value.cancelledList!!
                            _cancelledState.value =
                                _cancelledState.value.copy(cancelledList = newCancelledList)
                        }


                    }

                    is ResultState.Error -> {

                    }
                }
            }
        }

    }

    fun deliveredAction() {
        viewModelScope.launch {
            getUserOrdersUseCase(SELLER_ORDER_STATUS.COMPLETED).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _deliveredState.value = _deliveredState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _deliveredState.value =
                            _deliveredState.value.copy(isLoading = false, deliveredList = it.data)
                    }

                    is ResultState.Error -> {
                        _deliveredState.value =
                            _deliveredState.value.copy(isLoading = false, errorMessage = it.message)
                    }
                }
            }

        }
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

    fun cancelAction() {
        viewModelScope.launch {
            getUserOrdersUseCase(SELLER_ORDER_STATUS.CANCELLED).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _cancelledState.value = _cancelledState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _cancelledState.value =
                            _cancelledState.value.copy(isLoading = false, cancelledList = it.data)
                    }

                    is ResultState.Error -> {
                        _cancelledState.value =
                            _cancelledState.value.copy(isLoading = false, errorMessage = it.message)
                    }
                }
            }
        }
    }

    sealed class PurchasedEvent {
        data class PopToTab(val tabIndex: Int) : PurchasedEvent()
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
    val toConfirmList: List<SellerOrderDto>? = null,

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
    val cancelledList: List<SellerOrderDto>? = null
)


