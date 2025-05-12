package com.nrin31266.ecommercemultivendor.network

import com.nrin31266.ecommercemultivendor.common.constant.PRICE_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.RATING_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.common.constant.SORT_PRODUCTS
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.domain.dto.BannerDto
import com.nrin31266.ecommercemultivendor.domain.dto.CartDto
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.ReviewDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.WishlistItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateOrderRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateReviewRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.VerifyTokenRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.HomeCategoryResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.PaymentResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.UserWishlistProductResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.VerifyTokenResponse
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
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("sort") sort: SORT_PRODUCTS? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("search") search: String? = null,
        @Query("priceFilter") priceFilter: PRICE_FILTER? = null,
        @Query("ratingFilter") ratingFilter: RATING_FILTER?= null
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

    @POST("api/orders")
    suspend fun createOrder(
        @Header("Authorization") jwt: String,
        @Body request: CreateOrderRequest
    ): PaymentResponse

    @GET("api/orders/user")
    suspend fun getUserOrders(
        @Header("Authorization") jwt: String,
        @Query("status") status: SELLER_ORDER_STATUS,
    ): List<SellerOrderDto>

    @GET("api/orders/user/{sellerOrderId}")
    suspend fun getUserOrderDetails(
        @Header("Authorization") jwt: String,
        @Path("sellerOrderId") sellerOrderId: Long,
    ): SellerOrderDto

    @PUT("api/users/orders/seller-order/cancel/{sellerOrderId}")
    suspend fun userCancelSellerOrder(
        @Header("Authorization") jwt: String,
        @Path("sellerOrderId") sellerOrderId: Long,
    ): SellerOrderDto


    @PUT("api/users/orders/seller-order/confirm/{sellerOrderId}")
    suspend fun userConfirmSellerOrder(
        @Header("Authorization") jwt: String,
        @Path("sellerOrderId") sellerOrderId: Long,
    ): SellerOrderDto

    @POST("api/reviews/users/{orderItemId}")
    suspend fun addReview(
        @Header("Authorization") jwt: String,
        @Path("orderItemId") orderItemId: Long,
        @Body rq: CreateReviewRequest
    ): ReviewDto

    @GET("reviews/{productId}")
    suspend fun getReviewsByProductId(
        @Path("productId") productId: Long,
    ): List<ReviewDto>

    @GET("reviews/first/{productId}")
    suspend fun getFirstReviewByProductId(
        @Path("productId") productId: Long,
    ): List<ReviewDto>

    @GET("api/orders/item/{orderItem}")
    suspend fun getOrderItem(
        @Header("Authorization") jwt: String,
        @Path("orderItem") orderItem: Long,
    ): OrderItemDto

    @GET("home/categories")
    suspend fun getHomeCategories(): HomeCategoryResponse

    @GET("home/banners")
    suspend fun getHomeBanners(): List<BannerDto>

    @GET("api/wishlist/user")
    suspend fun getUserWishlist(
        @Header("Authorization") jwt: String
    ): List<WishlistItemDto>

    @POST("api/wishlist/user/{productId}")
    suspend fun addToWishlist(
        @Header("Authorization") jwt: String,
        @Path("productId") productId: Long
    ): UserWishlistProductResponse
    @POST("api/wishlist/user/check/{productId}")
    suspend fun checkWishlist(
        @Header("Authorization") jwt: String,
        @Path("productId") productId: Long
    ): UserWishlistProductResponse

}