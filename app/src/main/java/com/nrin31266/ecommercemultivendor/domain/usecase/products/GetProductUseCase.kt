package com.nrin31266.ecommercemultivendor.domain.usecase.products

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.PRICE_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.RATING_FILTER
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.PageableDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(
        category: String? = null,
        sort: String? = null,
        pageNumber: Int? = null,
        search: String? = null,
        priceFilter: PRICE_FILTER? = null,
        ratingFilter: RATING_FILTER? = null
    ): Flow<ResultState<PageableDto<ProductDto>>> {
        return repo.getProducts(category, sort, pageNumber, search, priceFilter, ratingFilter)
    }
}