package com.nrin31266.ecommercemultivendor.domain.repo

import android.net.Uri
import com.nrin31266.ecommercemultivendor.common.ResultState
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
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateOrderRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateReviewRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.VerifyTokenRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.HomeCategoryResponse
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.PaymentResponse
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
    sort: SORT_PRODUCTS? = null,
    pageNumber: Int? = null,
    search: String? = null,
    priceFilter: PRICE_FILTER? = null,
    ratingFilter: RATING_FILTER?= null
    ): Flow<ResultState<PageableDto<ProductDto>>>

    fun getProductDetail(id: Long): Flow<ResultState<ProductDto>>
    fun addToCart(productId: Long, subProductId: Long, request: AddUpdateCartItemRequest): Flow<ResultState<CartItemDto>>
    fun updateCartItem(cartItemId: Long, request: AddUpdateCartItemRequest): Flow<ResultState<CartItemDto>>
    fun deleteCartItem(cartItemId: Long): Flow<ResultState<ApiResponseNoData>>
    fun getUserCart(): Flow<ResultState<CartDto>>
    fun getAllUserAddresses(): Flow<ResultState<List<AddressDto>>>
    fun addUserAddress(addressDto: AddressDto): Flow<ResultState<AddressDto>>
    fun getDefaultUserAddress(): Flow<ResultState<AddressDto>>
    fun updateUserAddress(addressId: Long, addressDto: AddressDto): Flow<ResultState<AddressDto>>
    fun deleteUserAddress(addressId: Long): Flow<ResultState<ApiResponseNoData>>
    fun createOrder(request: CreateOrderRequest): Flow<ResultState<PaymentResponse>>
    fun getUserOrders(status: SELLER_ORDER_STATUS): Flow<ResultState<List<SellerOrderDto>>>
    fun getUserOrderDetails(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>>
    fun userCancelSellerOrder(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>>
    fun userConfirmSellerOrder(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>>
    fun getReviewsByProductId(productId: Long): Flow<ResultState<List<ReviewDto>>>
    fun addReview(productId: Long, rq: CreateReviewRequest, uris: List<Uri>): Flow<ResultState<ReviewDto>>
    fun getFirstReviewByProductId(productId: Long): Flow<ResultState<List<ReviewDto>>>
    fun getOrderItem(orderItem: Long): Flow<ResultState<OrderItemDto>>
    fun getHomeCategories(): Flow<ResultState<HomeCategoryResponse>>
    fun getHomeBanners(): Flow<ResultState<List<BannerDto>>>




}