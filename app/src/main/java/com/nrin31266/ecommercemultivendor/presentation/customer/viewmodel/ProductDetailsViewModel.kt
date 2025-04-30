package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SubProductDto
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

    private val _currentSubProduct = MutableStateFlow<SubProductDto?>(null)
    val currentSubProduct: StateFlow<SubProductDto?> = _currentSubProduct


    val selectedOptions = mutableStateMapOf<String, String>()
    val mapOptions = mutableStateMapOf<String, List<String>>()
    val mapSubProducts = mutableStateMapOf<String, SubProductDto>()
    val mapKeySubProductImages = mutableStateMapOf<String, String>()
    val mapKeyToOptionMap = mutableStateMapOf<String, Map<String, String>>()


    fun getProductDetails(id: Long) {
        viewModelScope.launch {
            getProductDetailsUseCase(id).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                    }

                    is ResultState.Success -> {
                        val product = it.data
                        buildOptionsFromProduct(product)
                        _state.value = _state.value.copy(
                            currentProduct = product,
                            isLoading = false,
                            quantity = 1
                        )
                    }

                    is ResultState.Error -> {
                        _state.value =
                            _state.value.copy(errorMessage = it.message, isLoading = false)
                    }
                }
            }

        }
    }

    fun updateSelectedOption(type: String, value: String) {
        if (value == "") selectedOptions.remove(type)
        else selectedOptions[type] = value
        _currentSubProduct.value = findMatchingSubProduct(selectedOptions)
    }

    fun updateQuantity(quantity: Int) {
        _state.value = _state.value.copy(quantity = quantity)
    }


    private fun clearOptionStates() {
        selectedOptions.clear()
        mapOptions.clear()
        mapSubProducts.clear()
        mapKeySubProductImages.clear()
        mapKeyToOptionMap.clear()
        _currentSubProduct.value = null
    }

    private fun buildOptionsFromProduct(product: ProductDto?) {
        clearOptionStates()

        if (product != null && !product.isSubProduct && product.subProducts != null) {
            val tempMap = mutableMapOf<String, MutableSet<String>>()

            product.subProducts!!.forEach { subProduct ->
                val optionMap = mutableMapOf<String, String>()

                subProduct.options?.forEach { option ->
                    val type = option.optionType?.value ?: return@forEach
                    val value = option.optionValue ?: return@forEach

                    tempMap.getOrPut(type) { mutableSetOf() }.add(value)
                    optionMap[type] = value

                    if (product.optionKey != null && mapKeySubProductImages[value] == null) {
                        mapKeySubProductImages[value] = subProduct.images?.firstOrNull() ?: ""
                    }
                }

                if (optionMap.isNotEmpty()) {
                    val optionKey = optionKeyString(optionMap)
                    mapSubProducts[optionKey] = subProduct
                    mapKeyToOptionMap[optionKey] = optionMap
                }
            }

            tempMap.forEach { (type, values) ->
                mapOptions[type] = values.toList()
            }
        }
    }

    private fun optionKeyString(optionMap: Map<String, String>): String =
        optionMap.entries.sortedBy { it.key }.joinToString("|") { "${it.key}:${it.value}" }


    private fun findMatchingSubProduct(selectedOptions: Map<String, String>): SubProductDto? {
        return mapSubProducts[optionKeyString(selectedOptions)]
    }

}

data class ProductDetailsState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var currentProduct: ProductDto? = null,
    var favCurrentProduct: Boolean = true,
    var quantity: Int = 1

)
