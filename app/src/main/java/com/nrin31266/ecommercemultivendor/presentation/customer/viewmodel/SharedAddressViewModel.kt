package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.domain.usecase.address.AddUserAddressUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.address.DeleteUserAddressUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.address.GetAllUserAddressUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.address.GetDefaultUserAddressUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.address.UpdateUserAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedAddressViewModel @Inject constructor(
    private val getAllUserAddressUseCase: GetAllUserAddressUseCase,
    private val addUserAddressUseCase: AddUserAddressUseCase,
    private val updateUserAddressUseCase: UpdateUserAddressUseCase,
    private val deleteUserAddressUseCase: DeleteUserAddressUseCase,
    private val getDefaultUserAddressUseCase: GetDefaultUserAddressUseCase
) : ViewModel() {


    private val _sharedAddressState = MutableStateFlow(SharedAddressState())
    val sharedAddressState: StateFlow<SharedAddressState> = _sharedAddressState.asStateFlow()
    private val _addressState = MutableStateFlow(AddressState())
    val addressState: StateFlow<AddressState> = _addressState.asStateFlow()
    private val _addressActionState = MutableStateFlow(AddressActionState())
    val addressActionState: StateFlow<AddressActionState> = _addressActionState.asStateFlow()
    private val _addressEvent = MutableStateFlow<AddressEvent?>(null)
    val addressEvent: StateFlow<AddressEvent?> = _addressEvent.asStateFlow()

    fun selectAddress(address: AddressDto) {
        _sharedAddressState.value = _sharedAddressState.value.copy(selectedAddress = address)
    }


    fun getAllUserAddresses() {
        viewModelScope.launch {
            getAllUserAddressUseCase().collect { it ->
                when (it) {
                    is ResultState.Loading -> {
                        _addressState.value = _addressState.value.copy(loading = true)
                    }

                    is ResultState.Success -> {
                        _addressState.value =
                            _addressState.value.copy(addresses = it.data, loading = false)
                    }

                    is ResultState.Error -> {
                        _addressState.value =
                            _addressState.value.copy(errorMessage = it.message, loading = false)
                    }
                }

            }
        }
    }

    fun getDefaultUserAddress() {
        viewModelScope.launch {
            getDefaultUserAddressUseCase().collect {
                when (it) {

                    is ResultState.Loading -> {
                        _sharedAddressState.value = _sharedAddressState.value.copy(loading = true)
                    }

                    is ResultState.Success -> {
                        _sharedAddressState.value = _sharedAddressState.value.copy(
                            selectedAddress = it.data,
                            loading = false
                        )
                    }

                    is ResultState.Error -> {
                        _sharedAddressState.value = _sharedAddressState.value.copy(
                            errorMessage = it.message,
                            loading = false
                        )
                    }
                }

            }
        }
    }

    fun addUserAddress(addressDto: AddressDto) {
        viewModelScope.launch {
            addUserAddressUseCase(addressDto).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addressActionState.value = _addressActionState.value.copy(actionLoading = true)
                    }

                    is ResultState.Success -> {
                        _addressActionState.value =
                            _addressActionState.value.copy(address = it.data, actionLoading = false)
                        afterAddUpdateRemoveAddress(it.data, false)
                        _addressEvent.emit(AddressEvent.SentNav)
                    }

                    is ResultState.Error -> {
                        _addressActionState.value = _addressActionState.value.copy(
                            errorMessage = it.message,
                            actionLoading = false
                        )
                    }

                }
            }
        }
    }

    fun updateUserAddress(addressDto: AddressDto, addressId: Long) {
        viewModelScope.launch {
            updateUserAddressUseCase(addressDto, addressId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addressActionState.value = _addressActionState.value.copy(actionLoading = true)
                    }

                    is ResultState.Success -> {
                        _addressActionState.value =
                            _addressActionState.value.copy(address = it.data, actionLoading = false)
                        afterAddUpdateRemoveAddress(it.data, false)
                    }

                    is ResultState.Error -> {
                        _addressActionState.value = _addressActionState.value.copy(
                            errorMessage = it.message,
                            actionLoading = false
                        )
                    }
                }
            }
        }
    }

    fun deleteUserAddress(address: AddressDto) {
        viewModelScope.launch {

            deleteUserAddressUseCase(address.id!!).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addressActionState.value = _addressActionState.value.copy(actionLoading = true)
                    }

                    is ResultState.Success -> {
                        _addressActionState.value = _addressActionState.value.copy(actionLoading = false)
                        afterAddUpdateRemoveAddress(address, true)
                    }

                    is ResultState.Error -> {
                        _addressActionState.value = _addressActionState.value.copy(
                            errorMessage = it.message,
                            actionLoading = false
                        )
                    }
                }
            }
        }
    }


    private fun afterAddUpdateRemoveAddress(address: AddressDto, isRemove: Boolean) {
        if (isRemove && _sharedAddressState.value.selectedAddress?.id == address.id) {
            if (_addressState.value.addresses.size > 1) {
                _sharedAddressState.value =
                    _sharedAddressState.value.copy(selectedAddress = _addressState.value.addresses[0])
            } else {
                _sharedAddressState.value = _sharedAddressState.value.copy(selectedAddress = null)
            }
        }
//        getAllUserAddresses()
    }

}

data class AddressState(
    val addresses: List<AddressDto> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null,
)

data class SharedAddressState(
    val selectedAddress: AddressDto? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null
)

data class AddressActionState(
    val address: AddressDto? = null,
    val loading: Boolean = false,
    val actionLoading: Boolean = false,
    val errorMessage: String? = null
)


sealed class AddressEvent{
    data object SentNav : AddressEvent()
    data object ShowDialog: AddressEvent()
}
