package com.nrin31266.ecommercemultivendor.domain.usecase.auth

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.VerifyTokenRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.VerifyTokenResponse
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyTokenUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(verifyTokenRequest: VerifyTokenRequest): Flow<ResultState<VerifyTokenResponse>> {
        return repo.verifyToken(verifyTokenRequest)
    }
}