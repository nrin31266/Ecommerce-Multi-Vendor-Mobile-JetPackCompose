package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.os.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.SellerLoginUseCase
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
    private val sellerLoginUseCase: SellerLoginUseCase,
    private val sendEmailOtpUseCase: SendEmailOtpUseCase,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    private val _userAuthState = MutableStateFlow(UserAuthState())
    val userAuthState: StateFlow<UserAuthState> = _userAuthState

    private val _sellerAuthState = MutableStateFlow(SellerAuthState())
    val sellerAuthState: StateFlow<SellerAuthState> = _sellerAuthState

    fun sendEmailOtp(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            when (authRequest.role) {
                USER_ROLE.ROLE_SELLER.toString() -> {
                    sendEmailOtpUseCase(authRequest).collect { result ->
                        when (result) {
                            is ResultState.Loading -> {
                                _sellerAuthState.value = SellerAuthState(isLoading = true)
                            }

                            is ResultState.Success -> {

                                _sellerAuthState.value =
                                    SellerAuthState(isSentOtp = true, isLoading = false)
                            }

                            is ResultState.Error -> {
                                _sellerAuthState.value = SellerAuthState(
                                    errorMessage = result.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }

                in listOf(USER_ROLE.ROLE_CUSTOMER.toString(), USER_ROLE.ROLE_ADMIN.toString()) -> {
                    sendEmailOtpUseCase(authRequest).collect { result ->
                        when (result) {
                            is ResultState.Loading -> {
                                _userAuthState.value = UserAuthState(isLoading = true)
                            }

                            is ResultState.Success -> {
                                _userAuthState.value =
                                    UserAuthState(isSentOtp = true, isLoading = false)
                            }

                            is ResultState.Error -> {
                                _userAuthState.value =
                                    UserAuthState(errorMessage = result.message, isLoading = false)
                            }
                        }
                    }
                }

                else -> {

                }


            }
        }
    }


    fun userSignup(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userSignupUseCase(authRequest).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userAuthState.value = UserAuthState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        authPreferences.saveAuth(result.data.jwt, USER_ROLE.ROLE_CUSTOMER.toString())
                        _userAuthState.value =
                            UserAuthState(jwt = result.data.jwt, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _userAuthState.value =
                            UserAuthState(errorMessage = result.message, isLoading = false)
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
                        _userAuthState.value = UserAuthState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        authPreferences.saveAuth(result.data.jwt, USER_ROLE.ROLE_CUSTOMER.toString())
                        _userAuthState.value =
                            UserAuthState(jwt = result.data.jwt, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _userAuthState.value =
                            UserAuthState(errorMessage = result.message, isLoading = false)
                    }

                }

            }
        }
    }

    fun sellerLogin(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            sellerLoginUseCase(authRequest).collect { result ->

                when (result) {
                    is ResultState.Loading -> {
                        _sellerAuthState.value = SellerAuthState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        authPreferences.saveAuth(result.data.jwt, USER_ROLE.ROLE_CUSTOMER.toString())
                        _sellerAuthState.value =
                            SellerAuthState(jwt = result.data.jwt, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _sellerAuthState.value =
                            SellerAuthState(errorMessage = result.message, isLoading = false)
                    }
                }

            }

        }


    }

    fun logout() {
        viewModelScope.launch {
            authPreferences.clearAuth()
            _sellerAuthState.value = SellerAuthState()
            _userAuthState.value = UserAuthState()
        }
    }

}

data class SellerAuthState(
    val isLoading: Boolean = false,
    val jwt: String? = null,
    val errorMessage: String? = null,
    val isSentOtp: Boolean = false,
    )

data class UserAuthState(
    val isLoading: Boolean = false,
    val jwt: String? = null,
    val errorMessage: String? = null,
    val isSentOtp: Boolean = false,
    )