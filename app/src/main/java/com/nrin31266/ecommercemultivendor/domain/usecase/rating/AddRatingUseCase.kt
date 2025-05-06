package com.nrin31266.ecommercemultivendor.domain.usecase.rating

import android.net.Uri
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.ReviewDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateOrderRequest
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateReviewRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.PaymentResponse
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRatingUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(orderId: Long, rq: CreateReviewRequest,uris: List<Uri>): Flow<ResultState<ReviewDto>> {
        return repo.addReview(orderId, rq ,uris)
    }
}