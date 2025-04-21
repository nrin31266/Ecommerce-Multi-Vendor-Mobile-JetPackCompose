package com.nrin31266.ecommercemultivendor.domain.repo

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun userSignup(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
    fun userLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//    fun sellerLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
    fun getUserProfile(jwt: String): Flow<ResultState<UserDto>>
//    fun getSellerProfile(jwt: String): Flow<ResultState<SellerDto>>
    fun sendEmailOtp(authRequest: AuthRequest): Flow<ResultState<ApiResponseNoData>>
}