package com.nrin31266.ecommercemultivendor.network

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

//fun userSignup(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//fun userLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//fun sellerLogin(authRequest: AuthRequest): Flow<ResultState<AuthResponse>>
//fun getUserProfile(jwt: String): Flow<ResultState<UserDto>>
//fun getSellerProfile(jwt: String): Flow<ResultState<SellerDto>>
interface ApiService {
    @POST("auth/signup")
    suspend fun userSignup(@Body request: AuthRequest): AuthResponse
    @POST("auth/signing")
    suspend fun userLogin(@Body request: AuthRequest): AuthResponse
//    @POST("sellers/login")
//    suspend fun sellerLogin(@Body request: AuthRequest): AuthResponse
    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") jwt: String): UserDto
//    @GET("sellers/profile")
//    suspend fun getSellerProfile(@Header("Authorization") jwt: String): SellerDto
    @POST("auth/send-login-signup-otp")
    suspend fun sendEmailOtp(@Body request: AuthRequest): ApiResponseNoData
    @POST("auth/valid-token")
    suspend fun verifyToken(@Body request: VerifyTokenRequest): VerifyTokenResponse
//    @RequestParam(required = false) String category,
//    @RequestParam(required = false) String brand,
//    @RequestParam(required = false) String colors,
//    @RequestParam(required = false) String sizes,
//    @RequestParam(required = false) Integer minimumPrice,
//    @RequestParam(required = false) Integer maximumPrice,
//    @RequestParam(required = false) Integer minimumDiscount,
//    @RequestParam(required = false) String sort,
//    @RequestParam(required = false) String stock,
//    @RequestParam(defaultValue = "1", required = false) Integer pageNumber,
//    @RequestParam(required = false) String search
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("sort") sort: String? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("search") search: String? = null,

    ): PageableDto<ProductDto>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: Long,
    ): ProductDto

    @PUT("api/cart/add/{productId}/item/{subProductId}")
    suspend fun addToCart(
        @Path("productId") productId: Long,
        @Path("subProductId") subProductId: Long,
        @Body request: AddUpdateCartItemRequest,
        @Header("Authorization") jwt: String
    ): CartItemDto

    @PUT("api/cart/update/item/{cartItemId}")
    suspend fun updateCartItem(
        @Path("cartItemId") cartItemId: Long,
        @Body request: AddUpdateCartItemRequest,
        @Header("Authorization") jwt: String
    ): CartItemDto

    @DELETE("api/cart/item/{cartItemId}")
    suspend fun deleteCartItem(
        @Path("cartItemId") cartItemId: Long,
        @Header("Authorization") jwt: String
    ): ApiResponseNoData

    @GET("api/cart")
    suspend fun getCart(
        @Header("Authorization") jwt: String
    ): CartDto

    @GET("api/users/address")
    suspend fun getAllUserAddresses(
        @Header("Authorization") jwt: String
    ) : List<AddressDto>

    @POST("api/users/address")
    suspend fun addUserAddress(
        @Header("Authorization") jwt: String,
        @Body addressDto: AddressDto
    ) : AddressDto

    @GET("api/users/address/default")
    suspend fun getDefaultUserAddress(
        @Header("Authorization") jwt: String
    ) : AddressDto

    @PUT("api/users/address/{addressId}")
    suspend fun updateUserAddress(
        @Header("Authorization") jwt: String,
        @Path("addressId") addressId: Long,
        @Body addressDto: AddressDto
    ) : AddressDto

    @DELETE("api/users/address/{addressId}")
    suspend fun deleteUserAddress(
        @Header("Authorization") jwt: String,
        @Path("addressId") addressId: Long
    ): ApiResponseNoData
}