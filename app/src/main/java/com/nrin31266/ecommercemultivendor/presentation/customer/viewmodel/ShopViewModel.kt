package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.usecase.seller.GetSellerProfileUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.seller.GetShopProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val getShopInfoUseCase: GetSellerProfileUseCase,
    private val getShopProductsUseCase: GetShopProductsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ShopState())
    val state: StateFlow<ShopState> = _state.asStateFlow()

    fun getShopInfo(sellerId: Long) {
        viewModelScope.launch {
            getShopInfoUseCase(sellerId).collect {

                when (it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(shopInfoLoading = true)
                    }

                    is ResultState.Success -> {
                        _state.value =
                            _state.value.copy(shopInfoLoading = false, shopInfo = it.data)
                    }

                    is ResultState.Error -> {
                        _state.value =
                            _state.value.copy(shopInfoLoading = false, shopInfoError = it.message)
                    }


                }
            }

        }
    }

    fun getShopProducts(sellerId: Long) {
        viewModelScope.launch {

            getShopProductsUseCase(sellerId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(shopProductsLoading = true)
                    }

                    is ResultState.Success -> {
                        _state.value =
                            _state.value.copy(shopProductsLoading = false, shopProducts = it.data)
                    }

                    is ResultState.Error -> {
                        _state.value =
                            _state.value.copy(
                                shopProductsLoading = false,
                                shopProductsError = it.message
                            )
                    }
                }
            }
        }

    }
}

data class ShopState(
    val shopInfoLoading: Boolean = false,
    val shopProductsLoading: Boolean = false,
    val shopInfo: SellerDto? = null,
    val shopProducts: List<ProductDto> = emptyList(),
    val shopInfoError: String = "",
    val shopProductsError: String = ""
)