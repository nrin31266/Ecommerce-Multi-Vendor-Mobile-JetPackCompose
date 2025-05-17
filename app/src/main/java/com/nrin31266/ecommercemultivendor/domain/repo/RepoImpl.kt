package com.nrin31266.ecommercemultivendor.domain.repo


import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.PRICE_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.RATING_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.common.constant.SORT_PRODUCTS
import com.nrin31266.ecommercemultivendor.common.toReadableError
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.domain.dto.BannerDto
import com.nrin31266.ecommercemultivendor.domain.dto.CartDto
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.PaymentDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.ReviewDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
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
import com.nrin31266.ecommercemultivendor.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class RepoImpl @Inject constructor(private val apiService: ApiService,private val authPreferences: AuthPreferences,
    private val firebaseStorage: FirebaseStorage) : Repo {
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
            emit(ResultState.Error(e.toReadableError()))
        }  catch (e: Exception) {
            emit(ResultState.Error("Unknown error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserProfile(): Flow<ResultState<UserDto>> = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.getUserProfile(token) })
    }.flowOn(Dispatchers.IO)

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
        sort: SORT_PRODUCTS?,
        pageNumber: Int?,
        search: String?
        , priceFilter: PRICE_FILTER?,
        ratingFilter: RATING_FILTER?
    ): Flow<ResultState<PageableDto<ProductDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getProducts(category, sort, pageNumber, search, priceFilter, ratingFilter) })
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

    override fun getUserCart(): Flow<ResultState<CartDto>> = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.getCart(token) })
    }.flowOn(Dispatchers.IO)

    override fun getAllUserAddresses(): Flow<ResultState<List<AddressDto>>> = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.getAllUserAddresses(token) })
    }.flowOn(Dispatchers.IO)

    override fun addUserAddress(addressDto: AddressDto): Flow<ResultState<AddressDto>> = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.addUserAddress(token, addressDto) })
    }.flowOn(Dispatchers.IO)

    override fun getDefaultUserAddress(): Flow<ResultState<AddressDto>>  = flow{
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.getDefaultUserAddress(token) })
    }.flowOn(Dispatchers.IO)

    override fun updateUserAddress(
        addressId: Long,
        addressDto: AddressDto
    ): Flow<ResultState<AddressDto>>  = flow{
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.updateUserAddress(token, addressId, addressDto) })
    }.flowOn(Dispatchers.IO)

    override fun deleteUserAddress(addressId: Long): Flow<ResultState<ApiResponseNoData>>  = flow {
        emit(ResultState.Loading)
        val token = getBearerToken()
        emit(makeApiCall { apiService.deleteUserAddress(token, addressId) })
    }.flowOn(Dispatchers.IO)

    override fun createOrder(request: CreateOrderRequest): Flow<ResultState<PaymentResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.createOrder(getBearerToken(), request) })
    }.flowOn(Dispatchers.IO)

    override fun getUserOrders(status: SELLER_ORDER_STATUS): Flow<ResultState<List<SellerOrderDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getUserOrders(getBearerToken(), status) })
    }.flowOn(Dispatchers.IO)

    override fun getUserOrderDetails(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getUserOrderDetails(getBearerToken(), sellerOrderId) })
    }.flowOn(Dispatchers.IO)

    override fun userCancelSellerOrder(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.userCancelSellerOrder(getBearerToken(), sellerOrderId) })
    }.flowOn(Dispatchers.IO)

    override fun userConfirmSellerOrder(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.userConfirmSellerOrder(getBearerToken(), sellerOrderId) })
    }.flowOn(Dispatchers.IO)

    override fun getReviewsByProductId(productId: Long): Flow<ResultState<List<ReviewDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getReviewsByProductId(productId) })
    }.flowOn(Dispatchers.IO)

    override fun addReview(productId: Long, rq: CreateReviewRequest, uris: List<Uri>): Flow<ResultState<ReviewDto>> = flow {
        emit(ResultState.Loading)
        if(uris.isNotEmpty()){
            val reviewImages = uris.map { uri ->
                uploadImageToFirebase(uri, "review_images", firebaseStorage)
            }
            rq.reviewImages = reviewImages
        }
        emit(makeApiCall { apiService.addReview(getBearerToken(),productId, rq) })
    }.flowOn(Dispatchers.IO)

    override fun getFirstReviewByProductId(productId: Long): Flow<ResultState<List<ReviewDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getFirstReviewByProductId(productId) })
    }.flowOn(Dispatchers.IO)

    override fun getOrderItem(orderItem: Long): Flow<ResultState<OrderItemDto>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getOrderItem(getBearerToken(), orderItem) })
    }.flowOn(Dispatchers.IO)

    override fun getHomeCategories(): Flow<ResultState<HomeCategoryResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getHomeCategories() })
    }.flowOn(Dispatchers.IO)

    override fun getHomeBanners(): Flow<ResultState<List<BannerDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getHomeBanners() })
    }.flowOn(Dispatchers.IO)

    override fun getUserWishlist(): Flow<ResultState<List<WishlistItemDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getUserWishlist(getBearerToken()) })
    }.flowOn(Dispatchers.IO)

    override fun addToWishlist(productId: Long): Flow<ResultState<UserWishlistProductResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.addToWishlist(getBearerToken(), productId) })
    }.flowOn(Dispatchers.IO)

    override fun isUserWishlist(productId: Long): Flow<ResultState<UserWishlistProductResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.checkWishlist(getBearerToken(), productId) })
    }.flowOn(Dispatchers.IO)

    override fun getRelatedProducts(productId: Long): Flow<ResultState<List<ProductDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getRelatedProducts(productId) })
    }.flowOn(Dispatchers.IO)

    override fun userPendingPayment(): Flow<ResultState<List<PaymentDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.userPendingPayment(getBearerToken()) })
    }.flowOn(Dispatchers.IO)

    override fun userCancelPayment(paymentId: Long): Flow<ResultState<PaymentDto>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.userCancelPayment(getBearerToken(), paymentId) })
    }.flowOn(Dispatchers.IO)

    override fun rePayOrder(paymentId: Long): Flow<ResultState<PaymentResponse>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.rePayOrder(getBearerToken(), paymentId) })
    }.flowOn(Dispatchers.IO)

    override fun getSellerProfile(sellerId: Long): Flow<ResultState<SellerDto>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getSellerProfile(sellerId) })
    }.flowOn(Dispatchers.IO)

    override fun getSellerProducts(sellerId: Long): Flow<ResultState<List<ProductDto>>> = flow {
        emit(ResultState.Loading)
        emit(makeApiCall { apiService.getSellerProducts(sellerId) })
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
            ResultState.Error(e.toReadableError())
        } catch (e: Exception) {
            ResultState.Error("Unknown error: ${e.localizedMessage}")
        }
    }

    private suspend fun uploadImageToFirebase(
        uri: Uri,
        folder: String? = "review_images",
        firebaseStorage: FirebaseStorage
    ): String = suspendCancellableCoroutine { cont ->
        val filename = "${System.currentTimeMillis()}_${uri.lastPathSegment}"
        val storageRef = firebaseStorage.reference.child("$folder/$filename")

        storageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Upload failed")
                }
                storageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUrl ->
                cont.resume(downloadUrl.toString(), null)
            }
            .addOnFailureListener { e ->
                cont.resumeWithException(e)
            }
    }


}