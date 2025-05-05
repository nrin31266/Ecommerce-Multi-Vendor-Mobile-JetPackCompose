package com.nrin31266.ecommercemultivendor.domain.usecase.payment

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserOrdersUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(status: SELLER_ORDER_STATUS): Flow<ResultState<List<SellerOrderDto>>> {
        return repo.getUserOrders(status)
    }
}