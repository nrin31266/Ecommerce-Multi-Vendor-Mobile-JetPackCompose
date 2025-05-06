package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateReviewRequest
import com.nrin31266.ecommercemultivendor.domain.usecase.rating.AddRatingUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.rating.GetOrderItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRatingViewModel @Inject constructor(
    private val addRatingUseCase: AddRatingUseCase,
    private val getOrderItemUseCase: GetOrderItemUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddRatingState())
    val state = _state.asStateFlow()

    private val _handle = MutableStateFlow(AddRatingHandle())
    val handle = _handle.asStateFlow()
    private val _event = MutableSharedFlow<AddRatingEvent>()
    val event = _event.asSharedFlow()

    fun getOrderItem(orderId: Long) {
        viewModelScope.launch {
            getOrderItemUseCase(orderId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = AddRatingState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _state.value = AddRatingState(orderItem = it.data)
                    }

                    is ResultState.Error -> {
                        _state.value = AddRatingState(errorMessage = it.message)
                    }
                }
            }
        }
    }

    fun addRating(orderId: Long, rq: CreateReviewRequest, uris: List<Uri>) {
        viewModelScope.launch {
            addRatingUseCase(orderId, rq, uris).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _handle.value = AddRatingHandle(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _event.emit(AddRatingEvent.Rated)
                    }

                    is ResultState.Error -> {
                        _handle.value = AddRatingHandle(errorMessage = it.message)
                    }
                }
            }

        }
    }
    sealed class AddRatingEvent{
        data object Rated: AddRatingEvent()
    }

}

data class AddRatingState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orderItem: OrderItemDto? = null
)

data class AddRatingHandle(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)