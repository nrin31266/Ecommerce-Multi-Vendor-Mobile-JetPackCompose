package com.nrin31266.ecommercemultivendor.domain.usecase.address

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserAddressUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(addressId: Long): Flow<ResultState<ApiResponseNoData>> {
        return repo.deleteUserAddress(addressId)
    }
}