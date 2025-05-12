package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.WishlistItemDto
import com.nrin31266.ecommercemultivendor.domain.usecase.wishlist.GetUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getUserWishlistUseCase: GetUserWishlistUseCase
) : ViewModel() {
    private val _wishlistState = MutableStateFlow(WishlistState())
    val wishlistState = _wishlistState.asStateFlow()

    fun getUserWishlist() {
        viewModelScope.launch {
            getUserWishlistUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _wishlistState.value = WishlistState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _wishlistState.value = WishlistState(data = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _wishlistState.value =
                            WishlistState(errorMessage = it.message, isLoading = false)
                    }
                }
            }
        }
    }
}

data class WishlistState(
    val isLoading: Boolean = false,
    val data: List<WishlistItemDto> = emptyList(),
    val errorMessage: String? = null

)