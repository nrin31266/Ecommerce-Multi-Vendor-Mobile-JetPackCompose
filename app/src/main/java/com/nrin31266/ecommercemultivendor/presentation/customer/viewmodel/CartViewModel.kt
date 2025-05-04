package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartDto
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ShopCartGroupResponse
import com.nrin31266.ecommercemultivendor.domain.usecase.cart.AddProductToCartUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.cart.DeleteCartItemUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.cart.GetUserCartUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.cart.UpdateCartItemUseCase
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductDetailsViewModel.ProductDetailsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val getUserCartUseCase: GetUserCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase
) : ViewModel() {
    private val _addProductToCartState = MutableStateFlow(ProductDetailsAddItemToCartState())
    val addProductToCartState: StateFlow<ProductDetailsAddItemToCartState> = _addProductToCartState

    private val _addProductToCartEventFlow = MutableSharedFlow<ProductDetailsEvent>()
    val addProductToCartEventFlow = _addProductToCartEventFlow.asSharedFlow()

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    private val _cartInfoState = MutableStateFlow(CartInfoState())
    val cartInfoState: StateFlow<CartInfoState> = _cartInfoState

    private val _cartItemState = MutableStateFlow(CartItemState())
    val cartItemState: StateFlow<CartItemState> = _cartItemState

//    private val _selectedItems = MutableStateFlow<Set<String>>(emptySet())
//    val selectedItems = _selectedItems.asStateFlow()

    fun updateCartItem(cartItemId: Long, request: AddUpdateCartItemRequest) {
        viewModelScope.launch {
            updateCartItemUseCase(cartItemId, request).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _cartItemState.value = _cartItemState.value.copy(
                            mapEnable = _cartItemState.value.mapEnable + (cartItemId.toString() to false)
                        )
                    }

                    is ResultState.Success -> {

                        _cartItemState.value = _cartItemState.value.copy(
                            mapEnable = _cartItemState.value.mapEnable - (cartItemId.toString()),
                            mapError = _cartItemState.value.mapError - (cartItemId.toString())
                        )
                        afterAddUpdateRemoveCartItem(it.data)

                    }

                    is ResultState.Error -> {
                        _cartItemState.value = _cartItemState.value.copy(
                            mapEnable = _cartItemState.value.mapEnable - (cartItemId.toString()),
                            mapError = _cartItemState.value.mapError + (cartItemId.toString() to it.message)
                        )
                    }
                }

            }
        }
    }

    fun deleteCartItem(cartItemId: Long, cartItem: CartItemDto) {
        viewModelScope.launch {

            deleteCartItemUseCase(cartItemId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _cartItemState.value = _cartItemState.value.copy(
                            mapEnable = _cartItemState.value.mapEnable + (cartItemId.toString() to false),
                            mapCartItem = _cartItemState.value.mapCartItem + (cartItemId.toString() to cartItem)
                        )
                    }

                    is ResultState.Success -> {
                        afterAddUpdateRemoveCartItem(cartItem, true)
                        _cartItemState.value = _cartItemState.value.copy(
                            mapEnable = _cartItemState.value.mapEnable - (cartItemId.toString()),
                            mapError = _cartItemState.value.mapError - (cartItemId.toString()),
                            mapCartItem = _cartItemState.value.mapCartItem - (cartItemId.toString()),

                            )

                    }

                    is ResultState.Error -> {
                        _cartItemState.value = _cartItemState.value.copy(
                            mapEnable = _cartItemState.value.mapEnable - (cartItemId.toString()),
                            mapError = _cartItemState.value.mapError + (cartItemId.toString() to it.message),
                            mapCartItem = _cartItemState.value.mapCartItem - (cartItemId.toString()),

                            )
                    }
                }
            }
        }

    }


    fun getUserCart() {
        viewModelScope.launch {
            getUserCartUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )

                    }

                    is ResultState.Success -> {
                        calculateCartInfo()
                        _state.value = _state.value.copy(
                            isLoading = false,
                            cart = it.data
                        )
                        calculateCartInfo()
                    }

                    is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


    fun addProductToCart(productId: Long, subProductId: Long, request: AddUpdateCartItemRequest) {
        viewModelScope.launch {
            addProductToCartUseCase(productId, subProductId, request).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addProductToCartState.value =
                            _addProductToCartState.value.copy(isLoading = true, errorMessage = null)
                    }

                    is ResultState.Success -> {
                        _addProductToCartState.value = _addProductToCartState.value.copy(
                            isLoading = false,
                        )
                        _addProductToCartEventFlow.emit(ProductDetailsEvent.ShowSnackbar("Added to cart successfully"))


                        afterAddUpdateRemoveCartItem(it.data)

                    }

                    is ResultState.Error -> {
                        _addProductToCartState.value = _addProductToCartState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                        _addProductToCartEventFlow.emit(ProductDetailsEvent.ShowBasicDialog(it.message))
                    }
                }
            }

        }
    }

    fun changeShowDialog(value: Boolean, cartItem: CartItemDto? = null) {
        _state.value = _state.value.copy(
            showDialog = value,
            currentCartItem = cartItem
        )
    }

    private fun afterAddUpdateRemoveCartItem(cartItem: CartItemDto, isRemove: Boolean = false) {
        val seller = cartItem.product?.seller ?: return

        val cart = _state.value.cart ?: return
        val groups = cart.groups.toMutableList()
        val groupIndex = groups.indexOfFirst { it.seller.id == seller.id }
        if (groupIndex == -1) {
            if (!isRemove) {
                groups.add(
                    ShopCartGroupResponse(
                        seller,
                        listOf(cartItem)
                    )
                )
            }
        } else {
            val group = groups[groupIndex]
            val items = group.cartItems.toMutableList()
            if (isRemove) {
                items.removeIf { it.id == cartItem.id }
            } else {
                val index = items.indexOfFirst { it.id == cartItem.id }
                if (index != -1) {
                    items[index] = cartItem // update tại chỗ
                } else {
                    items.add(cartItem) // mới thì thêm vào cuối
                }
            }
            if (items.isEmpty()) {
                groups.removeAt(groupIndex)
            } else {
                groups[groupIndex] = group.copy(cartItems = items)
            }
        }
        _state.value = _state.value.copy(
            cart = cart.copy(groups = groups)
        )

        calculateCartInfo()
    }


    private fun calculateCartInfo() {
        val cart = _state.value.cart ?: return

        var totalShop = 0
        var totalItem = 0
        var totalSellingPrice = 0L
        var totalMrpPrice = 0L
        var totalCartItem = 0
        var totalCartItemAvailable = 0;


        cart.groups.forEach { group ->
            totalShop += 1
            group.cartItems.forEach { item ->
//                mapLoading[item.id.toString()] = false
//                mapError[item.id.toString()] = ""

                val subProduct = item.subProduct!!
                val rootQuantity = subProduct.quantity!!
                val quantity = item.quantity!!

                totalCartItem += 1
                if (quantity > rootQuantity) return@forEach

                val sellingPrice = subProduct.sellingPrice ?: return@forEach
                val mrpPrice = subProduct.mrpPrice ?: return@forEach


                totalSellingPrice += sellingPrice * quantity
                totalMrpPrice += mrpPrice * quantity
                totalItem += quantity
                totalCartItemAvailable += 1
            }
        }

        _cartInfoState.value = CartInfoState(
            totalShop = totalShop,
            totalItem = totalItem,
            totalSellingPrice = totalSellingPrice,
            totalMrpPrice = totalMrpPrice,
            discountPercentage = if (totalMrpPrice == 0L) 0 else ((totalMrpPrice - totalSellingPrice) * 100 / totalMrpPrice).toInt(),
            totalCartItem = totalCartItem,
            totalCartItemAvailable = totalCartItemAvailable
        )
//        _cartItemState.value = CartItemState(
//            mapLoading = mapLoading,
//            mapError = mapError
//        )


    }


    private fun clearCart() {
        _state.value = _state.value.copy(
            cart = null
        )
        _cartInfoState.value = CartInfoState()
    }

}

data class ProductDetailsAddItemToCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

data class CartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val cart: CartDto? = null,
    val currentCartItem: CartItemDto? = null,
    val showDialog: Boolean = false,
)

data class CartInfoState(
    val totalShop: Int = 0,
    val totalItem: Int = 0,
    val totalSellingPrice: Long = 0L,
    val totalMrpPrice: Long = 0L,
    val discountPercentage: Int = 0,
    val totalCartItem: Int = 0,
    val totalCartItemAvailable: Int = 0,
)

data class CartItemState(
    val mapEnable: Map<String, Boolean> = emptyMap(),
    val mapError: Map<String, String> = emptyMap(),
    val mapCartItem: Map<String, CartItemDto> = emptyMap(),
)