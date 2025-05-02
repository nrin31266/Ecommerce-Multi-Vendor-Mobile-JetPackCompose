package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedAddressViewModel @Inject constructor() : ViewModel() {


    private val _sharedAddressState = MutableStateFlow(SharedAddressState())
    val sharedAddressState: StateFlow<SharedAddressState> = _sharedAddressState.asStateFlow()

    fun selectAddress(address: AddressDto) {
        _sharedAddressState.value = _sharedAddressState.value.copy(selectedAddress = address)
    }

}
data class SharedAddressState(
    val selectedAddress: AddressDto? = null,
    val addresses: List<AddressDto> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)
