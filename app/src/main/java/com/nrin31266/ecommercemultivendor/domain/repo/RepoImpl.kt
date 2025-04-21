package com.nrin31266.ecommercemultivendor.domain.repo


import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.toReadableErrorMoshi
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import com.nrin31266.ecommercemultivendor.network.ApiService
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepoImpl @Inject constructor(private val apiService: ApiService) : Repo {
    override fun userSignup(authRequest: AuthRequest): Flow<ResultState<AuthResponse>> {
        TODO("Not yet implemented")
    }

    override fun userLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.userLogin(authRequest)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

//    override fun sellerLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>> = flow {
//        emit(ResultState.Loading)
//        try {
//            val response = apiService.sellerLogin(authRequest)
//            emit(ResultState.Success(response))
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.message ?: "Unknown error"))
//        }
//
//
//    }.flowOn(Dispatchers.IO)

    override fun getUserProfile(jwt: String): Flow<ResultState<UserDto>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.getUserProfile(jwt)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

//    override fun getSellerProfile(jwt: String): Flow<ResultState<SellerDto>> = flow{
//
//        emit(ResultState.Loading)
//        try {
//            val response = apiService.getSellerProfile(jwt)
//            emit(ResultState.Success(response))
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.message ?: "Unknown error"))
//        }
//    }.flowOn(Dispatchers.IO)

    override fun sendEmailOtp(authRequest: AuthRequest): Flow<ResultState<ApiResponseNoData>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.sendEmailOtp(authRequest)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            emit(ResultState.Error(e.toReadableErrorMoshi()))
        }  catch (e: Exception) {
            emit(ResultState.Error("Unknown error: ${e.localizedMessage}"))
        }


    }.flowOn(Dispatchers.IO)

}