package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SubProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.usecase.cart.AddProductToCartUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.products.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    val getProductDetailsUseCase: GetProductDetailsUseCase,
    val addProductToCartUseCase: AddProductToCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailsState())
    val state: StateFlow<ProductDetailsState> = _state



    private val _productOptionState = MutableStateFlow(ProductOptionState())
    val productOptionState: StateFlow<ProductOptionState> = _productOptionState



    fun getProductDetails(id: Long) {
        viewModelScope.launch {
            getProductDetailsUseCase(id).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, errorMessage = null, quantity = 1,
                            isOpenSheetBottom = false)
                    }

                    is ResultState.Success -> {
                        val product = it.data
                        buildOptionsFromProduct(product)
                        _state.value = _state.value.copy(
                            currentProduct = product,
                            isLoading = false,
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
        if (value == "") {
            _productOptionState.value = _productOptionState.value.copy(
                selectedOptions = _productOptionState.value.selectedOptions - type
            )
        } else {
            _productOptionState.value = _productOptionState.value.copy(
                selectedOptions = _productOptionState.value.selectedOptions + (type to value)
            )
        }
        _state.value = _state.value.copy(currentSubProductDto = findMatchingSubProduct(_productOptionState.value.selectedOptions))
    }

    fun updateQuantity(quantity: Int) {
        _state.value = _state.value.copy(quantity = quantity)
    }

    fun changeIsSheetBottom() {
        _state.value = _state.value.copy(isOpenSheetBottom = !_state.value.isOpenSheetBottom)
    }

    private fun clearOptionStates() {
        _productOptionState.value = ProductOptionState()
        _state.value = _state.value.copy(currentSubProductDto = null)
    }

    private fun buildOptionsFromProduct(product: ProductDto?) {
        clearOptionStates()

        if (product != null && !product.isSubProduct && product.subProducts != null) {
            val tempMapOptions = mutableMapOf<String, MutableSet<String>>()
            val newMapSubProducts = mutableMapOf<String, SubProductDto>()
            val newMapKeyToOptionMap = mutableMapOf<String, Map<String, String>>()
            val newMapKeySubProductImages = mutableMapOf<String, String>()


            product.subProducts!!.forEach { subProduct ->
                val optionMap = mutableMapOf<String, String>()

                subProduct.options?.forEach { option ->
                    val type = option.optionType?.value ?: return@forEach
                    val value = option.optionValue ?: return@forEach

                    tempMapOptions.getOrPut(type) { mutableSetOf() }.add(value)
                    optionMap[type] = value

                    if (product.optionKey != null && newMapKeySubProductImages[value] == null) {
                        newMapKeySubProductImages[value] = subProduct.images?.firstOrNull() ?: ""
                    }
                }

                if (optionMap.isNotEmpty()) {
                    val optionKey = optionKeyString(optionMap)
                    newMapSubProducts[optionKey] = subProduct
                    newMapKeyToOptionMap[optionKey] = optionMap
                }
            }

            val finalMapOptions = tempMapOptions.mapValues { it.value.toList() }

            _productOptionState.value = _productOptionState.value.copy(
                mapOptions = finalMapOptions,
                mapSubProducts = newMapSubProducts,
                mapKeyToOptionMap = newMapKeyToOptionMap,
                mapKeySubProductImages = newMapKeySubProductImages
            )
        }else if(product != null && product.isSubProduct ){
            _state.value = _state.value.copy(currentSubProductDto = product.subProducts?.get(0))
        }
    }


    private fun optionKeyString(optionMap: Map<String, String>): String =
        optionMap.entries.sortedBy { it.key }.joinToString("|") { "${it.key}:${it.value}" }

    private fun findMatchingSubProduct(selectedOptions: Map<String, String>): SubProductDto? {
        return _productOptionState.value.mapSubProducts[optionKeyString(selectedOptions)]
    }

    sealed class ProductDetailsEvent {
        data class ShowSnackbar(val message: String) : ProductDetailsEvent()
        data class ShowBasicDialog(val message: String) : ProductDetailsEvent()
    }

}

data class ProductDetailsState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var currentProduct: ProductDto? = null,
    var favCurrentProduct: Boolean = true,
    var quantity: Int = 1,
    var isOpenSheetBottom: Boolean = false,
    var currentSubProductDto: SubProductDto?= null
)

data class ProductOptionState(
    val selectedOptions: Map<String, String> = emptyMap(),
    val mapOptions: Map<String, List<String>> = emptyMap(),
    val mapSubProducts: Map<String, SubProductDto> = emptyMap(),
    val mapKeySubProductImages: Map<String, String> = emptyMap(),
    val mapKeyToOptionMap: Map<String, Map<String, String>> = emptyMap()
)


