package com.nrin31266.ecommercemultivendor.domain.usecase.seller

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopProductsUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(sellerId: Long): Flow<ResultState<List<ProductDto>>> {
        return repo.getSellerProducts(sellerId)
    }
}