package com.nrin31266.ecommercemultivendor.domain.repo


import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.toReadableErrorMoshi
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.VerifyTokenRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.VerifyTokenResponse
import com.nrin31266.ecommercemultivendor.network.ApiService
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepoImpl @Inject constructor(private val apiService: ApiService,private val authPreferences: AuthPreferences) : Repo {
    override fun userSignup(authRequest: AuthRequest): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.userSignup(authRequest) })
    }.flowOn(Dispatchers.IO)

    override fun userLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.userLogin(authRequest)
            emit(ResultState.Success(response))
        }  catch (e: HttpException) {
            emit(ResultState.Error(e.toReadableErrorMoshi()))
        }  catch (e: Exception) {
            emit(ResultState.Error("Unknown error: ${e.localizedMessage}"))
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
        } catch (e: HttpException) {
            emit(ResultState.Error(e.toReadableErrorMoshi()))
        }  catch (e: Exception) {
            emit(ResultState.Error("Unknown error: ${e.localizedMessage}"))
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
        emit(makeApiCall { apiService.sendEmailOtp(authRequest) })
    }.flowOn(Dispatchers.IO)

    override fun verifyToken(verifyTokenRequest: VerifyTokenRequest): Flow<ResultState<VerifyTokenResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.verifyToken(verifyTokenRequest) })
    }.flowOn(Dispatchers.IO)

    override fun getProducts(
        category: String?,
        sort: String?,
        pageNumber: Int?,
        search: String?
    ): Flow<ResultState<PageableDto<ProductDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getProducts(category, sort, pageNumber, search) })
    }.flowOn(Dispatchers.IO)

    override fun getProductDetail(id: Long): Flow<ResultState<ProductDto>> = flow{
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getProductDetail(id) })
    }.flowOn(Dispatchers.IO)

    override fun addToCart(
        productId: Long,
        subProductId: Long,
        request: AddUpdateCartItemRequest
    ): Flow<ResultState<CartItemDto>> = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.addToCart(productId, subProductId, request, token) })

    }.flowOn(Dispatchers.IO)

    override fun updateCartItem(
        cartItemId: Long,
        request: AddUpdateCartItemRequest
    ): Flow<ResultState<CartItemDto>> = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.updateCartItem(cartItemId, request, token) })

    }.flowOn(Dispatchers.IO)

    override fun deleteCartItem(cartItemId: Long): Flow<ResultState<ApiResponseNoData>> = flow{
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.deleteCartItem(cartItemId, token) })
    }.flowOn(Dispatchers.IO)

    private suspend fun getBearerToken(): String {
        val token = authPreferences.jwtFlow.firstOrNull()
            ?: throw IllegalStateException("Unauthorized")
        return "Bearer $token"
    }


    private suspend fun <T> makeApiCall(apiCall: suspend () -> T): ResultState<T> {
        return try {
            val response = apiCall()
            ResultState.Success(response)
        } catch (e: HttpException) {
            ResultState.Error(e.toReadableErrorMoshi())
        } catch (e: Exception) {
            ResultState.Error("Unknown error: ${e.localizedMessage}")
        }
    }



}