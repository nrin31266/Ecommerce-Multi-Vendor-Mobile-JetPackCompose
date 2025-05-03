package com.nrin31266.ecommercemultivendor.domain.usecase.address

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserAddressUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(addressDto: AddressDto, addressId: Long): Flow<ResultState<AddressDto>> {
        return repo.updateUserAddress(addressId, addressDto)
    }
}