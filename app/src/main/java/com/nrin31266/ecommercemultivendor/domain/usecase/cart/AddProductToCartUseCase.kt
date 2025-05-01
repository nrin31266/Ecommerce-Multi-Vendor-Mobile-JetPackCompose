package com.nrin31266.ecommercemultivendor.domain.usecase.cart

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(productId: Long, subProductId: Long, request: AddUpdateCartItemRequest): Flow<ResultState<CartItemDto>> {
        return repo.addToCart(productId, subProductId, request)
    }
}