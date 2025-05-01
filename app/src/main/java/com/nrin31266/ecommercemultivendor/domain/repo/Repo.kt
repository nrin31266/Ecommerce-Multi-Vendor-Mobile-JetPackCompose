package com.nrin31266.ecommercemultivendor.domain.repo

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartDto
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
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface Repo {
    fun userSignup(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
    fun userLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//    fun sellerLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
    fun getUserProfile(): Flow<ResultState<UserDto>>
//    fun getSellerProfile(jwt: String): Flow<ResultState<SellerDto>>
    fun sendEmailOtp(authRequest: AuthRequest): Flow<ResultState<ApiResponseNoData>>

    fun verifyToken(verifyTokenRequest: VerifyTokenRequest): Flow<ResultState<VerifyTokenResponse>>
//    @Query("category") category: String? = null,
//    @Query("sort") sort: String? = null,
//    @Query("pageNumber") pageNumber: Int? = null,
//    @Query("search") search: String? = null,
    fun getProducts(
        category: String? = null,
        sort: String? = null,
        pageNumber: Int? = null,
        search: String? = null,
    ): Flow<ResultState<PageableDto<ProductDto>>>

    fun getProductDetail(id: Long): Flow<ResultState<ProductDto>>
    fun addToCart(productId: Long, subProductId: Long, request: AddUpdateCartItemRequest): Flow<ResultState<CartItemDto>>
    fun updateCartItem(cartItemId: Long, request: AddUpdateCartItemRequest): Flow<ResultState<CartItemDto>>
    fun deleteCartItem(cartItemId: Long): Flow<ResultState<ApiResponseNoData>>
    fun getUserCart(): Flow<ResultState<CartDto>>
}