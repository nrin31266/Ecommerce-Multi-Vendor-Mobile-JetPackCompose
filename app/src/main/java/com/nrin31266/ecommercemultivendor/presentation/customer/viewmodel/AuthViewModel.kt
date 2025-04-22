package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.VerifyTokenRequest
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.SendEmailOtpUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.UserLoginUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.UserSignupUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.VerifyTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userSignupUseCase: UserSignupUseCase,
    private val userLoginUseCase: UserLoginUseCase,

    private val sendEmailOtpUseCase: SendEmailOtpUseCase,
    private val authPreferences: AuthPreferences,
    private val verifyTokenUseCase: VerifyTokenUseCase
) : ViewModel() {
    private val _userAuthState = MutableStateFlow(UserAuthState())
    val userAuthState: StateFlow<UserAuthState> = _userAuthState
    private val signingPrefix = "signing_"
    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent = _authEvent.asSharedFlow()

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
                                errorMessage = null,
                                isLogin = true
                            )
                        _authEvent.emit(AuthEvent.NavigateToHome)
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
                                errorMessage = null,
                                isLogin = true
                            )
                        _authEvent.emit(AuthEvent.NavigateToHome)
                    }

                    is ResultState.Error -> {
                        _userAuthState.value =
                            _userAuthState.value.copy(errorMessage = result.message, isLoading = false)
                    }

                }

            }
        }
    }
    fun sendEmailOtp(authRequest: AuthRequest, isLogin:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            _userAuthState.value = _userAuthState.value.copy(
                currentEmail = authRequest.email
            )
            if(isLogin){
                authRequest.email = signingPrefix + authRequest.email
            }

            sendEmailOtpUseCase(authRequest).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userAuthState.value = _userAuthState.value.copy(
                            isLoading = true,
                        )
                    }

                    is ResultState.Success -> {
                        _userAuthState.value =
                            _userAuthState.value.copy(
                                isSentOtp = true, isLoading = false,
                                errorMessage = null,
                                timeLeft = 30
                            )
                        startCountdown()
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



    fun logout() {
        viewModelScope.launch {
            authPreferences.clearAuth()
            _userAuthState.value = _userAuthState.value.copy(
                jwt = null,
                isLogin = false
            )
        }
    }
    private var countdownJob: Job? = null

    private fun startCountdown() {
        countdownJob?.cancel() // Hủy job cũ nếu có

        countdownJob = viewModelScope.launch {
            while ((_userAuthState.value.timeLeft) > 0) {
                delay(1000)
                _userAuthState.value = _userAuthState.value.copy(
                    timeLeft = (_userAuthState.value.timeLeft ?: 1) - 1
                )
            }
        }
    }
    init {
        viewModelScope.launch {
            authPreferences.jwtFlow.collect { jwt ->
                if (!jwt.isNullOrBlank()) {
                    verifyTokenUseCase(VerifyTokenRequest("Bearer $jwt")).collect {
                        when (it) {
                            is ResultState.Loading -> {
                                _userAuthState.value = _userAuthState.value.copy(
                                    isInitLoading = true
                                )
                            }
                            is ResultState.Success -> {
                                _userAuthState.value = _userAuthState.value.copy(
                                    isInitLoading = false,
                                    isInitErrorMessage = null,
                                    jwt = jwt,
                                    isLogin = true
                                )
                            }
                            is ResultState.Error -> {
                                _userAuthState.value = _userAuthState.value.copy(
                                    isInitLoading = false,
                                    isInitErrorMessage = it.message
                                )

                            }
                        }
                    }
                }
            }
        }
    }



}








data class UserAuthState(
    val isLoading: Boolean = false,
    val jwt: String? = null,
    val errorMessage: String? = null,
    val isSentOtp: Boolean = false,
    val currentEmail: String? = null,
    val timeLeft:Int = 0,
    val isInitLoading : Boolean = false,
    val isInitErrorMessage: String? = null,
    val isLogin: Boolean= false,
)

sealed class AuthEvent {
    object NavigateToHome : AuthEvent()
    data class ShowSnackbar(val message: String) : AuthEvent()
}
