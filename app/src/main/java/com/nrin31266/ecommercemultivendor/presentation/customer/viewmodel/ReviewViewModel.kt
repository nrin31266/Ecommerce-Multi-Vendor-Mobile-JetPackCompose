package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.ReviewDto
import com.nrin31266.ecommercemultivendor.domain.usecase.rating.GetRatingsOfProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getRatingsOfProductUseCase: GetRatingsOfProductUseCase
): ViewModel(){
    private val _state = MutableStateFlow(ReviewState())
    val state: StateFlow<ReviewState> = _state.asStateFlow()

    fun getRatingsOfProduct(productId: Long){
        viewModelScope.launch {
            getRatingsOfProductUseCase(productId).collect{
                when(it){
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(loading = true)
                    }
                    is ResultState.Success -> {
                        _state.value = _state.value.copy(loading = false, data = it.data)
                    }
                    is ResultState.Error -> {
                        _state.value = _state.value.copy(loading = false, error = it.message)
                    }
                }
            }
        }
    }

}

data class ReviewState(
    val loading : Boolean = false,
    val error: String = "",
    val data: List<ReviewDto> = emptyList()
)