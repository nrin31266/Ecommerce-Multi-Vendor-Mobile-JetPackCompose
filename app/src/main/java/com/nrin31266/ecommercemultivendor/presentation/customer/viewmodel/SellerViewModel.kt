package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.usecase.sellers.GetSellerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor(
    private val getSellerProfileUseCase: GetSellerProfileUseCase
) : ViewModel() {
    private val _sellerState = MutableStateFlow(SellerState())
    val sellerState: StateFlow<SellerState> = _sellerState

    fun getSellerProfile(jwt: String) {
        viewModelScope.launch(Dispatchers.IO) {

            getSellerProfileUseCase(jwt).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _sellerState.value = SellerState(isGetSellerProfileLoading = true)


                    }

                    is ResultState.Success -> {
                        _sellerState.value = SellerState(
                            seller = it.data, isGetSellerProfileLoading = false, errorMessage = null
                        )

                    }

                    is ResultState.Error -> {

                        _sellerState.value = SellerState(
                            errorMessage = it.message,
                            isGetSellerProfileLoading = false
                        )

                    }
                }
            }
        }
    }

}

data class SellerState(
    val seller: SellerDto? = null,
    val isGetSellerProfileLoading: Boolean = false,
    val errorMessage: String? = null


)