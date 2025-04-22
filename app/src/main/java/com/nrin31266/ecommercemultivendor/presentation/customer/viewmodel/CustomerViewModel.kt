package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel(){

    private val _customerState = MutableStateFlow<CustomerState>(CustomerState())
    val customerState: StateFlow<CustomerState> = _customerState

    fun getUserProfile(jwt: String){
        viewModelScope.launch (Dispatchers.IO){
            getUserProfileUseCase(jwt).collect{
                when(it) {
                    is ResultState.Loading->{
                        _customerState.value = _customerState.value.copy(
                            isGetProfile = false
                        )

                    }
                    is ResultState.Success-> {
                        _customerState.value = _customerState.value.copy(
                            user = it.data,
                            isGetProfile = true
                        )
                    }
                    is ResultState.Error->{
                        _customerState.value = _customerState.value.copy(
                            errorMessage = it.message
                        )
                    }

                }
            }

        }
    }

}

data class CustomerState(
    val user: UserDto? = null,
    val isGetProfile: Boolean = false,
    val errorMessage: String? = null
)