package com.nrin31266.ecommercemultivendor.network

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

//fun userSignup(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//fun userLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//fun sellerLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//fun getUserProfile(jwt: String): Flow<ResultState<UserDto>>
//fun getSellerProfile(jwt: String): Flow<ResultState<SellerDto>>
interface ApiService {
    @POST("auth/signup")
    suspend fun userSignup(@Body request: AuthRequest): AuthResponse
    @POST("auth/login")
    suspend fun userLogin(@Body request: AuthRequest): AuthResponse
    @POST("sellers/login")
    suspend fun sellerLogin(@Body request: AuthRequest): AuthResponse
    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") jwt: String): UserDto
    @GET("sellers/profile")
    suspend fun getSellerProfile(@Header("Authorization") jwt: String): SellerDto
    @POST("auth/send-login-signup-otp")
    suspend fun sendEmailOtp(@Body request: AuthRequest): ApiResponse<Void>


}