package com.nrin31266.ecommercemultivendor.domain.usecase.seller

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSellerProfileUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(sellerId: Long): Flow<ResultState<SellerDto>> {
        return repo.getSellerProfile(sellerId)
    }
}