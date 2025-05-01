package com.nrin31266.ecommercemultivendor.domain.usecase.cart

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.CartDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserCartUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(): Flow<ResultState<CartDto>> {
        return repo.getUserCart()
    }
}