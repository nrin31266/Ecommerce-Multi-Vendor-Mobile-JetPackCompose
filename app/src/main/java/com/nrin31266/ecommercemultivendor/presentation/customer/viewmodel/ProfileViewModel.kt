package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
):ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun getUserProfile(){
       viewModelScope.launch {
           getUserProfileUseCase().collect{
               when(it){
                   is ResultState.Loading -> {
                       _state.value = _state.value.copy(
                           isLoading = true
                       )
                   }
                   is ResultState.Success -> {
                       _state.value = _state.value.copy(
                           isLoading = false,
                           userDto = it.data
                       )
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
}

data class ProfileState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userDto: UserDto?=null,
)