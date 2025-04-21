package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.os.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.SendEmailOtpUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.UserLoginUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.UserSignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userSignupUseCase: UserSignupUseCase,
    private val userLoginUseCase: UserLoginUseCase,

    private val sendEmailOtpUseCase: SendEmailOtpUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    private val _userAuthState = MutableStateFlow(UserAuthState())
    val userAuthState: StateFlow<UserAuthState> = _userAuthState


    fun sendEmailOtp(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {


            sendEmailOtpUseCase(authRequest).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userAuthState.value = _userAuthState.value.copy(
                            isLoading = true,
                            currentEmail = authRequest.email
                        )
                    }

                    is ResultState.Success -> {
                        _userAuthState.value =
                            _userAuthState.value.copy(
                                isSentOtp = true, isLoading = false,
                                errorMessage = null
                            )
                    }

                    is ResultState.Error -> {
                        _userAuthState.value =
                            _userAuthState.value.copy(
                                errorMessage = result.message,
                                isLoading = false
                            )
                    }
                }
            }
        }
    }
    fun userSignup(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userSignupUseCase(authRequest).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userAuthState.value = _userAuthState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        authPreferences.saveAuth(result.data.jwt, USER_ROLE.ROLE_CUSTOMER.toString())
                        _userAuthState.value =
                            _userAuthState.value.copy(
                                jwt = result.data.jwt, isLoading = false,
                                errorMessage = null
                            )
                    }

                    is ResultState.Error -> {
                        _userAuthState.value =
                            _userAuthState.value.copy(errorMessage = result.message, isLoading = false)
                    }
                }

            }

        }
    }

    fun userLogin(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userLoginUseCase(authRequest).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userAuthState.value = _userAuthState.value.copy(isLoading = true)
                    }

                    is ResultState.Success -> {
                        authPreferences.saveAuth(result.data.jwt, USER_ROLE.ROLE_CUSTOMER.toString())
                        _userAuthState.value =
                            _userAuthState.value.copy(
                                jwt = result.data.jwt, isLoading = false,
                                errorMessage = null
                            )
                    }

                    is ResultState.Error -> {
                        _userAuthState.value =
                            _userAuthState.value.copy(errorMessage = result.message, isLoading = false)
                    }

                }

            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            authPreferences.clearAuth()

        }
    }


}








data class UserAuthState(
    val isLoading: Boolean = false,
    val jwt: String? = null,
    val errorMessage: String? = null,
    val isSentOtp: Boolean = false,
    val currentEmail: String? = null
)