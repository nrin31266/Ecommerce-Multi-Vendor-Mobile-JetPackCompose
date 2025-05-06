package com.nrin31266.ecommercemultivendor.domain.usecase.purchased

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateOrderRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.PaymentResponse
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserCancelSellerOrderUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(sellerOrderId: Long): Flow<ResultState<SellerOrderDto>> {
        return repo.userCancelSellerOrder(sellerOrderId)
    }
}