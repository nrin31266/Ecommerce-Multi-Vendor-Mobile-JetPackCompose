package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.usecase.products.GetProductDetailsUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.products.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailsState())
    val state: StateFlow<ProductDetailsState> = _state


    fun getProductDetails(id: Long) {
        viewModelScope.launch {
            getProductDetailsUseCase(id).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                    }

                    is ResultState.Success -> {
                        _state.value = _state.value.copy(currentProduct = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _state.value = _state.value.copy(errorMessage = it.message, isLoading = false)
                    }
                }
            }

        }
    }

}

data class ProductDetailsState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var currentProduct: ProductDto? = null,
    var favCurrentProduct: Boolean = true

    )