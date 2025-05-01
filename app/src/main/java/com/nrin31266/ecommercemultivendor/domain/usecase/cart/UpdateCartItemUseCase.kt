package com.nrin31266.ecommercemultivendor.domain.usecase.cart

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(cartItemId: Long, request: AddUpdateCartItemRequest): Flow<ResultState<CartItemDto>> {
        return repo.updateCartItem(cartItemId, request)
    }
}